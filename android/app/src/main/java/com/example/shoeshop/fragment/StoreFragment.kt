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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.StoreProductAdapter
import com.example.shoeshop.model.Product
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog

class StoreFragment : Fragment() {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var rvStoreProducts: RecyclerView
    private lateinit var layoutEmptySearch: View
    private lateinit var etSearch: EditText

    private var masterProductList = listOf<Product>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val btnFilter = view.findViewById<LinearLayout>(R.id.btnFilter)
        rvStoreProducts = view.findViewById<RecyclerView>(R.id.rvStoreProducts)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        etSearch = view.findViewById(R.id.etSearch)
        layoutEmptySearch = view.findViewById(R.id.layoutEmptySearch)

        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        rvStoreProducts.visibility = View.GONE
        layoutEmptySearch.visibility = View.GONE

        rvStoreProducts.layoutManager = GridLayoutManager(context, 2)

        Handler(Looper.getMainLooper()).postDelayed({
            masterProductList = listOf(
               Product(1, "NIKE", "Air Max Dawn SE", "4.9", "1.2k", "2.450.000đ", R.drawable.banner_placeholder, "3.150.000đ", "-22%"),
                Product(2, "ADIDAS", "Ultraboost DNA", "4.8", "850", "3.200.000đ", R.drawable.banner_placeholder, "4.000.000đ", "-20%"),
              Product(3, "JORDAN", "Air Jordan 1 Low", "5.0", "620", "3.850.000đ", R.drawable.banner_placeholder, "3.850.000đ", ""),
              Product(4, "PUMA", "RS-X Efekt Futures", "4.7", "430", "2.100.000đ", R.drawable.banner_placeholder, "2.800.000đ", "-25%")
         )
            updateProductListUI(masterProductList)
        }, 2000)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val btnClearFilters = layoutEmptySearch.findViewById<Button>(R.id.btnClearFilters)
        btnClearFilters.setOnClickListener {
            etSearch.text?.clear()
            etSearch.clearFocus()
            filterProducts("")
        }

        btnFilter.setOnClickListener {
            showFilterBottomSheet()
        }

        return view
    }

    private fun filterProducts(query: String) {
        if (shimmerLayout.visibility == View.VISIBLE) return

        if (query.isEmpty()) {
            updateProductListUI(masterProductList)
        } else {
            val filteredList = masterProductList.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.brandName.contains(query, ignoreCase = true)
            }
            updateProductListUI(filteredList)
        }
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

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_filter_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val btnClose = bottomSheetView.findViewById<ImageView>(R.id.btnClose)
        val btnReset = bottomSheetView.findViewById<Button>(R.id.btnReset)
        val btnApply = bottomSheetView.findViewById<Button>(R.id.btnApply)

        var selectedGender = 1
        var selectedBrand = 1
        var selectedPriceRange = 0
        var selectedSortOption = 0
        var selectedSize = 36

        val chipGenderMale = bottomSheetView.findViewById<TextView>(R.id.chipGenderMale)
        val chipGenderFemale = bottomSheetView.findViewById<TextView>(R.id.chipGenderFemale)
        val chipGenderUnisex = bottomSheetView.findViewById<TextView>(R.id.chipGenderUnisex)

        fun updateGenderUI() {
            val genderChips = listOf(chipGenderMale, chipGenderFemale, chipGenderUnisex)
            genderChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedGender) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }
        }

        val chipBrandNike = bottomSheetView.findViewById<TextView>(R.id.chipBrandNike)
        val chipBrandAdidas = bottomSheetView.findViewById<TextView>(R.id.chipBrandAdidas)
        val chipBrandVans = bottomSheetView.findViewById<TextView>(R.id.chipBrandVans)
        val chipBrandConverse = bottomSheetView.findViewById<TextView>(R.id.chipBrandConverse)
        val chipBrandPuma = bottomSheetView.findViewById<TextView>(R.id.chipBrandPuma)

        fun updateBrandUI() {
            val brandChips = listOf(chipBrandNike, chipBrandAdidas, chipBrandVans, chipBrandConverse, chipBrandPuma)
            brandChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedBrand) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }
        }

        val chipPriceUnder2m = bottomSheetView.findViewById<TextView>(R.id.chipPriceUnder2m)
        val chipPrice2to4m = bottomSheetView.findViewById<TextView>(R.id.chipPrice2to4m)
        val chipPriceOver4m = bottomSheetView.findViewById<TextView>(R.id.chipPriceOver4m)

        fun updatePriceRangeUI() {
            val priceChips = listOf(chipPriceUnder2m, chipPrice2to4m, chipPriceOver4m)
            priceChips.forEachIndexed { index, chip ->
                if (index + 1 == selectedPriceRange) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }
        }

        val chipSortLowToHigh = bottomSheetView.findViewById<TextView>(R.id.chipSortLowToHigh)
        val chipSortHighToLow = bottomSheetView.findViewById<TextView>(R.id.chipSortHighToLow)

        fun updateSortUI() {
            if (selectedSortOption == 1) {
                chipSortLowToHigh.setBackgroundResource(R.drawable.bg_chip_selected)
                chipSortLowToHigh.setTextColor(resources.getColor(R.color.white, null))
                chipSortHighToLow.setBackgroundResource(R.drawable.bg_chip_unselected)
                chipSortHighToLow.setTextColor(resources.getColor(R.color.black, null))
            } else if (selectedSortOption == 2) {
                chipSortHighToLow.setBackgroundResource(R.drawable.bg_chip_selected)
                chipSortHighToLow.setTextColor(resources.getColor(R.color.white, null))
                chipSortLowToHigh.setBackgroundResource(R.drawable.bg_chip_unselected)
                chipSortLowToHigh.setTextColor(resources.getColor(R.color.black, null))
            } else {
                chipSortLowToHigh.setBackgroundResource(R.drawable.bg_chip_unselected)
                chipSortLowToHigh.setTextColor(resources.getColor(R.color.black, null))
                chipSortHighToLow.setBackgroundResource(R.drawable.bg_chip_unselected)
                chipSortHighToLow.setTextColor(resources.getColor(R.color.black, null))
            }
        }

        val sizeChips = listOf<TextView>(
            bottomSheetView.findViewById(R.id.chipSize36), bottomSheetView.findViewById(R.id.chipSize37),
            bottomSheetView.findViewById(R.id.chipSize38), bottomSheetView.findViewById(R.id.chipSize39),
            bottomSheetView.findViewById(R.id.chipSize40), bottomSheetView.findViewById(R.id.chipSize41),
            bottomSheetView.findViewById(R.id.chipSize42), bottomSheetView.findViewById(R.id.chipSize43)
        )

        fun updateSizeUI() {
            sizeChips.forEach { chip ->
                val sizeVal = chip.text.toString().toInt()
                if (sizeVal == selectedSize) {
                    chip.setBackgroundResource(R.drawable.bg_chip_selected)
                    chip.setTextColor(resources.getColor(R.color.white, null))
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                    chip.setTextColor(resources.getColor(R.color.black, null))
                }
            }
        }

        chipGenderMale.setOnClickListener { selectedGender = 1; updateGenderUI() }
        chipGenderFemale.setOnClickListener { selectedGender = 2; updateGenderUI() }
        chipGenderUnisex.setOnClickListener { selectedGender = 3; updateGenderUI() }

        chipBrandNike.setOnClickListener { selectedBrand = 1; updateBrandUI() }
        chipBrandAdidas.setOnClickListener { selectedBrand = 2; updateBrandUI() }
        chipBrandVans.setOnClickListener { selectedBrand = 3; updateBrandUI() }
        chipBrandConverse.setOnClickListener { selectedBrand = 4; updateBrandUI() }
        chipBrandPuma.setOnClickListener { selectedBrand = 5; updateBrandUI() }

        chipPriceUnder2m.setOnClickListener { selectedPriceRange = 1; updatePriceRangeUI() }
        chipPrice2to4m.setOnClickListener { selectedPriceRange = 2; updatePriceRangeUI() }
        chipPriceOver4m.setOnClickListener { selectedPriceRange = 3; updatePriceRangeUI() }

        chipSortLowToHigh.setOnClickListener { selectedSortOption = 1; updateSortUI() }
        chipSortHighToLow.setOnClickListener { selectedSortOption = 2; updateSortUI() }

        sizeChips.forEach { chip ->
            chip.setOnClickListener {
                selectedSize = chip.text.toString().toInt()
                updateSizeUI()
            }
        }

        btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

        btnReset.setOnClickListener {
            selectedGender = 1
            selectedBrand = 1
            selectedPriceRange = 0
            selectedSortOption = 0
            selectedSize = 36

            updateGenderUI()
            updateBrandUI()
            updatePriceRangeUI()
            updateSortUI()
            updateSizeUI()
        }

        btnApply.setOnClickListener {
            bottomSheetDialog.dismiss()

            val selectedBrandName = when(selectedBrand) {
                1 -> "NIKE"
                2 -> "ADIDAS"
                3 -> "VANS"
                4 -> "CONVERSE"
                5 -> "PUMA"
                else -> ""
            }

            filterProducts(selectedBrandName)
        }

        bottomSheetDialog.show()
    }
}