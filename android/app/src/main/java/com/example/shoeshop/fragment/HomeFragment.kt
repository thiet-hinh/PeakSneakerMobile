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
import com.example.shoeshop.retrofit.BrandRetrofit
import com.example.shoeshop.retrofit.ProductRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var rvBrands: RecyclerView
    private lateinit var rvFeatured: RecyclerView
    private lateinit var rvBestSeller: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvBrands = view.findViewById(R.id.rvBrands)
        rvFeatured = view.findViewById(R.id.rvFeaturedProducts)
        rvBestSeller = view.findViewById(R.id.rvBestSellers)

        rvBrands.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFeatured.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBestSeller.layoutManager = GridLayoutManager(context, 2)

        loadBrands()
        loadFeaturedOneEachBrand()
        loadBestSellers()

        return view
    }

    private fun loadBrands() {
        BrandRetrofit.api.getBrands().enqueue(object : Callback<List<Brand>> {
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                if (!isAdded) return
                if (response.isSuccessful && response.body() != null) {
                    rvBrands.adapter = BrandAdapter(response.body()!!)
                } else {
                    Toast.makeText(context, "Không thể tải danh sách thương hiệu", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(context, "Lỗi kết nối tải thương hiệu: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFeaturedOneEachBrand() {
        ProductRetrofit.api.getFeaturedOneEachBrand().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (!isAdded) return
                if (response.isSuccessful && response.body() != null) {
                    rvFeatured.adapter = ProductAdapter(response.body()!!, false)
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách nổi bật từ máy chủ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(context, "Lỗi kết nối tải sản phẩm nổi bật: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadBestSellers() {
        ProductRetrofit.api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (!isAdded) return
                if (response.isSuccessful && response.body() != null) {
                    rvBestSeller.adapter = ProductAdapter(response.body()!!, true)
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách sản phẩm", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(context, "Lỗi kết nối Server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}