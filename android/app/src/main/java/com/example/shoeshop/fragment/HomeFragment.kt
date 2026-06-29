package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.BrandAdapter
import com.example.shoeshop.adapter.ProductAdapter
import com.example.shoeshop.model.Brand
import com.example.shoeshop.model.Product
import com.example.shoeshop.retrofit.ProductRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var rvFeatured: RecyclerView
    private lateinit var rvBestSeller: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ánh xạ các RecyclerView từ fragment_home.xml
        val rvBrands = view.findViewById<RecyclerView>(R.id.rvBrands)
        rvFeatured = view.findViewById<RecyclerView>(R.id.rvFeaturedProducts)
        rvBestSeller = view.findViewById<RecyclerView>(R.id.rvBestSellers)

        // Danh mục thương hiệu hiển thị cục bộ (Khớp ID tương ứng từ Database)
        val brandList = listOf(
            Brand(1, "Nike", R.drawable.banner_placeholder),
            Brand(2, "Adidas", R.drawable.banner_placeholder),
            Brand(3, "Vans", R.drawable.banner_placeholder),
            Brand(4, "Converse", R.drawable.banner_placeholder),
            Brand(5, "Puma", R.drawable.banner_placeholder)
        )

        // Setup Danh sách thương hiệu cuộn ngang
        rvBrands.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBrands.adapter = BrandAdapter(brandList)

        // Cấu hình LayoutManager cho sản phẩm
        rvFeatured.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBestSeller.layoutManager = GridLayoutManager(context, 2)

        // Kích hoạt tiến trình kết nối dữ liệu từ Database
        loadDatabaseData()

        return view
    }

    private fun loadDatabaseData() {
        // 1. Lấy dữ liệu Sản phẩm nổi bật (Featured Products) từ Database
        ProductRetrofit.api.getFeaturedProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val featuredList = response.body()!!
                    rvFeatured.adapter = ProductAdapter(featuredList, false)
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách nổi bật từ máy chủ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối tải sản phẩm nổi bật: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // 2. Lấy toàn bộ Sản phẩm từ Database gán làm danh sách bán chạy
        ProductRetrofit.api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val bestSellerList = response.body()!!
                    rvBestSeller.adapter = ProductAdapter(bestSellerList, true)
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách sản phẩm", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối Server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}