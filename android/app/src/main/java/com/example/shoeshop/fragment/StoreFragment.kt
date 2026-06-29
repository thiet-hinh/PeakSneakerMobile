package com.example.shoeshop.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.StoreProductAdapter
import com.example.shoeshop.api.ProductApi
import com.example.shoeshop.model.Product
import com.example.shoeshop.retrofit.ProductRetrofit
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog

class StoreFragment : Fragment() {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var rvStoreProducts: RecyclerView
    private lateinit var layoutEmptySearch: View
    private lateinit var etSearch: EditText

    // Biến lưu trạng thái bộ lọc toàn cục
    private var currentKeyword: String? = null
    private var selectedGenderIdx = 0       // 1: MEN, 2: WOMEN, 3: UNISEX
    private var selectedBrandIdx = 0        // 1: Nike, 2: Adidas, 3: Vans, 4: Converse, 5: Puma
    private var selectedPriceRangeIdx = 0   // 1: Dưới 2m, 2: 2m-4m, 3: Trên 4m
    private var selectedSortOption = 0      // 1: Thấp -> Cao, 2: Cao -> Thấp
    private var selectedSizeVal: Int? = null

    // Bộ hoãn tránh spam API khi gõ chữ nhanh (Debounce)
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private lateinit var productApi: ProductApi
    // TODO: Khai báo API của bạn tại đây khi kết nối Retrofit
    // private lateinit var productApi: ProductApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val btnFilter = view.findViewById<LinearLayout>(R.id.btnFilter)
        rvStoreProducts = view.findViewById<RecyclerView>(R.id.rvStoreProducts)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        etSearch = view.findViewById(R.id.etSearch)
        layoutEmptySearch = view.findViewById(R.id.layoutEmptySearch)

        rvStoreProducts.layoutManager = GridLayoutManager(context, 2)
        productApi = ProductRetrofit.api
        loadProductsFromApi()

        // Xử lý sự kiện gõ chữ có hoãn 500ms tránh spam băm nát server
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                searchRunnable = Runnable {
                    currentKeyword = s.toString().trim().ifEmpty { null }
                    loadProductsFromApi()
                }
                searchHandler.postDelayed(searchRunnable!!, 500)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val btnClearFilters = layoutEmptySearch.findViewById<Button>(R.id.btnClearFilters)
        btnClearFilters.setOnClickListener {
            etSearch.text?.clear()
            etSearch.clearFocus()
            currentKeyword = null
            resetAllFiltersState()
            loadProductsFromApi()
        }

        btnFilter.setOnClickListener {
            showFilterBottomSheet()
        }

        return view
    }

    private fun loadProductsFromApi() {
        // 1. Trạng thái chuẩn bị: Bật hiệu ứng chờ Shimmer, ẩn danh sách và ẩn thông báo trống đi
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        rvStoreProducts.visibility = View.GONE
        layoutEmptySearch.visibility = View.GONE

        // 2. Ánh xạ dữ liệu từ bộ lọc UI sang các tham số chuẩn gửi lên Backend
        val genderParam = when (selectedGenderIdx) {
            1 -> "MEN"
            2 -> "WOMEN"
            3 -> "UNISEX"
            else -> null // Nếu chưa chọn -> truyền null để Backend lấy tất cả
        }

        val brandIdParam = if (selectedBrandIdx > 0) selectedBrandIdx else null

        var minPriceParam: Double? = null
        var maxPriceParam: Double? = null
        when (selectedPriceRangeIdx) {
            1 -> { minPriceParam = 0.0; maxPriceParam = 2000000.0 }
            2 -> { minPriceParam = 2000000.0; maxPriceParam = 4000000.0 }
            3 -> { minPriceParam = 4000000.0; maxPriceParam = 99999999.0 }
        }

        val sizeParam = selectedSizeVal?.toString()

        // 3. GỌI API THẬT QUA RETROFIT
        // Khi vừa vào màn hình, tất cả các tham số trên đều là null -> Backend sẽ chạy lệnh GET ALL PRODUCTS
        productApi.getProducts(currentKeyword, genderParam, brandIdParam, minPriceParam, maxPriceParam, sizeParam)
            .enqueue(object : retrofit2.Callback<List<Product>> {
                override fun onResponse(call: retrofit2.Call<List<Product>>, response: retrofit2.Response<List<Product>>) {
                    if (response.isSuccessful) {
                        var productList = response.body() ?: listOf()

                        // Xử lý sắp xếp tăng/giảm theo giá trực tiếp tại Client
                        if (productList.isNotEmpty()) {
                            productList = when (selectedSortOption) {
                                1 -> productList.sortedBy { it.price } // Sắp xếp giá thấp -> cao
                                2 -> productList.sortedByDescending { it.price } // Sắp xếp giá cao -> thấp
                                else -> productList // Giữ nguyên thứ tự mặc định của API
                            }
                        }

                        // Cập nhật lên giao diện danh sách sản phẩm lấy được
                        updateProductListUI(productList)
                    } else {
                        // Nếu lỗi Server (500, 404...), ẩn Shimmer và báo lỗi
                        updateProductListUI(listOf())
                        Toast.makeText(context, "Lỗi lấy dữ liệu từ máy chủ: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Product>>, t: Throwable) {
                    // Nếu mất mạng hoặc sập server không kết nối được
                    updateProductListUI(listOf())
                    Toast.makeText(context, "Không thể kết nối đến Server, vui lòng thử lại!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateProductListUI(productList: List<Product>) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE

        if (productList.isEmpty()) {
            rvStoreProducts.visibility = View.GONE
            layoutEmptySearch.visibility = View.VISIBLE
        } else {
            layoutEmptySearch.visibility = View.GONE
            rvStoreProducts.visibility = View.VISIBLE
            rvStoreProducts.adapter = StoreProductAdapter(productList)
        }
    }

    private fun resetAllFiltersState() {
        selectedGenderIdx = 0
        selectedBrandIdx = 0
        selectedPriceRangeIdx = 0
        selectedSortOption = 0
        selectedSizeVal = null
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_filter_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val btnClose = bottomSheetView.findViewById<ImageView>(R.id.btnClose)
        val btnReset = bottomSheetView.findViewById<Button>(R.id.btnReset)
        val btnApply = bottomSheetView.findViewById<Button>(R.id.btnApply)

        val chipGenderMale = bottomSheetView.findViewById<TextView>(R.id.chipGenderMale)
        val chipGenderFemale = bottomSheetView.findViewById<TextView>(R.id.chipGenderFemale)
        val chipGenderUnisex = bottomSheetView.findViewById<TextView>(R.id.chipGenderUnisex)
        val genderChips = listOf(chipGenderMale, chipGenderFemale, chipGenderUnisex)

        val chipBrandNike = bottomSheetView.findViewById<TextView>(R.id.chipBrandNike)
        val chipBrandAdidas = bottomSheetView.findViewById<TextView>(R.id.chipBrandAdidas)
        val chipBrandVans = bottomSheetView.findViewById<TextView>(R.id.chipBrandVans)
        val chipBrandConverse = bottomSheetView.findViewById<TextView>(R.id.chipBrandConverse)
        val chipBrandPuma = bottomSheetView.findViewById<TextView>(R.id.chipBrandPuma)
        val brandChips = listOf(chipBrandNike, chipBrandAdidas, chipBrandVans, chipBrandConverse, chipBrandPuma)

        val chipPriceUnder2m = bottomSheetView.findViewById<TextView>(R.id.chipPriceUnder2m)
        val chipPrice2to4m = bottomSheetView.findViewById<TextView>(R.id.chipPrice2to4m)
        val chipPriceOver4m = bottomSheetView.findViewById<TextView>(R.id.chipPriceOver4m)
        val priceChips = listOf(chipPriceUnder2m, chipPrice2to4m, chipPriceOver4m)

        val chipSortLowToHigh = bottomSheetView.findViewById<TextView>(R.id.chipSortLowToHigh)
        val chipSortHighToLow = bottomSheetView.findViewById<TextView>(R.id.chipSortHighToLow)

        val sizeChips = listOf<TextView>(
            bottomSheetView.findViewById(R.id.chipSize36), bottomSheetView.findViewById(R.id.chipSize37),
            bottomSheetView.findViewById(R.id.chipSize38), bottomSheetView.findViewById(R.id.chipSize39),
            bottomSheetView.findViewById(R.id.chipSize40), bottomSheetView.findViewById(R.id.chipSize41),
            bottomSheetView.findViewById(R.id.chipSize42), bottomSheetView.findViewById(R.id.chipSize43)
        )

        fun updateAllFilterUI() {
            genderChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedGenderIdx) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }

            brandChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedBrandIdx) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }

            priceChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedPriceRangeIdx) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }

            chipSortLowToHigh.setBackgroundResource(if (selectedSortOption == 1) R.drawable.bg_chip_selected else R.drawable.bg_chip_unselected)
            chipSortLowToHigh.setTextColor(resources.getColor(if (selectedSortOption == 1) R.color.white else R.color.black, null))
            chipSortHighToLow.setBackgroundResource(if (selectedSortOption == 2) R.drawable.bg_chip_selected else R.drawable.bg_chip_unselected)
            chipSortHighToLow.setTextColor(resources.getColor(if (selectedSortOption == 2) R.color.white else R.color.black, null))

            sizeChips.forEach { chip ->
                val sizeVal = chip.text.toString().toInt()
                if (sizeVal == selectedSizeVal) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }
        }

        updateAllFilterUI()

        chipGenderMale.setOnClickListener { selectedGenderIdx = if (selectedGenderIdx == 1) 0 else 1; updateAllFilterUI() }
        chipGenderFemale.setOnClickListener { selectedGenderIdx = if (selectedGenderIdx == 2) 0 else 2; updateAllFilterUI() }
        chipGenderUnisex.setOnClickListener { selectedGenderIdx = if (selectedGenderIdx == 3) 0 else 3; updateAllFilterUI() }

        chipBrandNike.setOnClickListener { selectedBrandIdx = if (selectedBrandIdx == 1) 0 else 1; updateAllFilterUI() }
        chipBrandAdidas.setOnClickListener { selectedBrandIdx = if (selectedBrandIdx == 2) 0 else 2; updateAllFilterUI() }
        chipBrandVans.setOnClickListener { selectedBrandIdx = if (selectedBrandIdx == 3) 0 else 3; updateAllFilterUI() }
        chipBrandConverse.setOnClickListener { selectedBrandIdx = if (selectedBrandIdx == 4) 0 else 4; updateAllFilterUI() }
        chipBrandPuma.setOnClickListener { selectedBrandIdx = if (selectedBrandIdx == 5) 0 else 5; updateAllFilterUI() }

        chipPriceUnder2m.setOnClickListener { selectedPriceRangeIdx = if (selectedPriceRangeIdx == 1) 0 else 1; updateAllFilterUI() }
        chipPrice2to4m.setOnClickListener { selectedPriceRangeIdx = if (selectedPriceRangeIdx == 2) 0 else 2; updateAllFilterUI() }
        chipPriceOver4m.setOnClickListener { selectedPriceRangeIdx = if (selectedPriceRangeIdx == 3) 0 else 3; updateAllFilterUI() }

        chipSortLowToHigh.setOnClickListener { selectedSortOption = if (selectedSortOption == 1) 0 else 1; updateAllFilterUI() }
        chipSortHighToLow.setOnClickListener { selectedSortOption = if (selectedSortOption == 2) 0 else 2; updateAllFilterUI() }

        sizeChips.forEach { chip ->
            chip.setOnClickListener {
                val sizeVal = chip.text.toString().toInt()
                selectedSizeVal = if (selectedSizeVal == sizeVal) null else sizeVal
                updateAllFilterUI()
            }
        }

        btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

        btnReset.setOnClickListener {
            resetAllFiltersState()
            updateAllFilterUI()
        }

        btnApply.setOnClickListener {
            bottomSheetDialog.dismiss()
            loadProductsFromApi()
        }

        bottomSheetDialog.show()
    }
}