package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.StoreProductAdapter
import com.example.shoeshop.model.Product
import com.google.android.material.bottomsheet.BottomSheetDialog

class StoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val btnFilter = view.findViewById<LinearLayout>(R.id.btnFilter)
        val rvStoreProducts = view.findViewById<RecyclerView>(R.id.rvStoreProducts)

        val storeProductList = listOf(
            Product(1, "NIKE", "Air Max Dawn SE", "4.9", "1.2k", "2.450.000đ", R.drawable.banner_placeholder, "3.150.000đ", "-22%"),
            Product(2, "ADIDAS", "Ultraboost DNA", "4.8", "850", "3.200.000đ", R.drawable.banner_placeholder, "4.000.000đ", "-20%"),
            Product(3, "JORDAN", "Air Jordan 1 Low", "5.0", "620", "3.850.000đ", R.drawable.banner_placeholder, "3.850.000đ", ""), // Không giảm giá
            Product(4, "PUMA", "RS-X Efekt Futures", "4.7", "430", "2.100.000đ", R.drawable.banner_placeholder, "2.800.000đ", "-25%")
        )

        rvStoreProducts.layoutManager = GridLayoutManager(context, 2)
        rvStoreProducts.adapter = StoreProductAdapter(storeProductList)

        btnFilter.setOnClickListener {
            showFilterBottomSheet()
        }

        return view
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_filter_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val btnClose = bottomSheetView.findViewById<ImageView>(R.id.btnClose)
        val btnReset = bottomSheetView.findViewById<Button>(R.id.btnReset)
        val btnApply = bottomSheetView.findViewById<Button>(R.id.btnApply)

        val chipSortLowToHigh = bottomSheetView.findViewById<TextView>(R.id.chipSortLowToHigh)
        val chipSortHighToLow = bottomSheetView.findViewById<TextView>(R.id.chipSortHighToLow)

        var selectedSortOption = 0

        fun updateSortUI() {
            if (selectedSortOption == 1) {
                chipSortLowToHigh.setBackgroundResource(R.drawable.bg_chip_selected)
                chipSortLowToHigh.setTextColor(resources.getColor(R.color.white, null))

                chipSortHighToLow.setBackgroundResource(R.drawable.bg_chip_unselected)
                chipSortHighToLow.setTextColor(resources.getColor(R.color.black, null)) // Hoặc mã màu #1C1B1B
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

        chipSortLowToHigh.setOnClickListener {
            selectedSortOption = 1
            updateSortUI()
        }
        chipSortHighToLow.setOnClickListener {
            selectedSortOption = 2
            updateSortUI()
        }
        btnClose.setOnClickListener { bottomSheetDialog.dismiss() }
        btnReset.setOnClickListener {
            selectedSortOption = 0
            updateSortUI()
        }
        btnApply.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}