package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.BrandAdapter
import com.example.shoeshop.adapter.ProductAdapter
import com.example.shoeshop.model.Brand // Sử dụng model của bạn
import com.example.shoeshop.model.Product // Sử dụng model của bạn

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ánh xạ các RecyclerView từ fragment_home.xml
        val rvBrands = view.findViewById<RecyclerView>(R.id.rvBrands)
        val rvFeatured = view.findViewById<RecyclerView>(R.id.rvFeaturedProducts)
        val rvBestSeller = view.findViewById<RecyclerView>(R.id.rvBestSellers)

        // 1. Khởi tạo dữ liệu mẫu cho Thương hiệu (Khớp với cấu trúc: id, name, imageResId)
        val brandList = listOf(
            Brand(1, "Nike", R.drawable.banner_placeholder),
            Brand(2, "Adidas", R.drawable.banner_placeholder),
            Brand(3, "Jordan", R.drawable.banner_placeholder),
            Brand(4, "Puma", R.drawable.banner_placeholder)
        )

        // 2. Dữ liệu mẫu cho Sản phẩm nổi bật (Khớp với: id, brandName, name, rating, sold, price, imageResId)
        val featuredList = listOf(
            Product(1, "NIKE SPORTSWEAR", "Air Max 270 Pro", "5.0", "| Đã bán 856", "3.500.000đ", R.drawable.banner_placeholder),
            Product(2, "ADIDAS PERFORMANCE", "Ultraboost Light", "4.9", "| Đã bán 1.2k", "4.200.000đ", R.drawable.banner_placeholder),
            Product(3, "JORDAN BRAND", "Air Jordan 1 Mid", "5.0", "| Đã bán 432", "4.850.000đ", R.drawable.banner_placeholder)
        )

        // 3. Dữ liệu mẫu cho Sản phẩm bán chạy nhất
        val bestSellerList = listOf(
            Product(4, "NIKE EDITION", "Dunk Low Panda", "4.8", "| Đã bán 2.1k", "3.150.000đ", R.drawable.banner_placeholder),
            Product(5, "ADIDAS ORIGINALS", "Forum Exhibit Low", "4.7", "| Đã bán 1.5k", "2.800.000đ", R.drawable.banner_placeholder),
            Product(6, "NEW BALANCE", "NB 550 White Grey", "4.9", "| Đã bán 980", "3.900.000đ", R.drawable.banner_placeholder),
            Product(7, "NIKE LIMITED", "Sacai x VaporWaffle", "5.0", "| Đã bán 124", "12.500.000đ", R.drawable.banner_placeholder)
        )

        // Setup 1: Danh sách thương hiệu cuộn ngang
        rvBrands.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBrands.adapter = BrandAdapter(brandList)

        // Setup 2: Sản phẩm nổi bật cuộn ngang (isGrid = false)
        rvFeatured.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFeatured.adapter = ProductAdapter(featuredList, false)

        // Setup 3: Sản phẩm bán chạy hiển thị dạng lưới 2 cột (isGrid = true)
        rvBestSeller.layoutManager = GridLayoutManager(context, 2)
        rvBestSeller.adapter = ProductAdapter(bestSellerList, true)

        return view
    }
}