package com.example.shoeshop.activity

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoeshop.R
import com.example.shoeshop.model.Product
import com.example.shoeshop.retrofit.ProductRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgDetailProduct: ImageView
    private lateinit var tvDetailBrand: TextView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailRating: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var tvDetailOriginalPrice: TextView
    private lateinit var tvDetailDiscount: TextView
    private lateinit var tvDetailDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        imgDetailProduct = findViewById(R.id.imgDetailProduct)
        tvDetailBrand = findViewById(R.id.tvDetailBrand)
        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailRating = findViewById(R.id.tvDetailRating)
        tvDetailPrice = findViewById(R.id.tvDetailPrice)
        tvDetailOriginalPrice = findViewById(R.id.tvDetailOriginalPrice)
        tvDetailDiscount = findViewById(R.id.tvDetailDiscount)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)

        btnBack.setOnClickListener { finish() }

        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1) {
            getProductDetailFromDatabase(productId)
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm này!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getProductDetailFromDatabase(id: Int) {
        ProductRetrofit.api.getProductById(id).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful && response.body() != null) {
                    val product = response.body()!!
                    populateProductData(product)
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Server báo lỗi mã: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Lỗi kết nối: ${t.localizedMessage}", Toast.LENGTH_LONG).show()

                android.util.Log.e("RETROFIT_ERR", "Bị lỗi mạng hoặc parse dữ liệu: ", t)
            }
        })
    }

    private fun populateProductData(product: Product) {
        val currencyFormat = NumberFormat.getInstance(Locale("vi", "VN"))

        tvDetailBrand.text = product.brandName
        tvDetailName.text = product.name
        tvDetailRating.text = product.rating
        tvDetailDescription.text = product.rawName ?: "Sản phẩm thời trang cao cấp với chất liệu bền bỉ, mang lại cảm giác thoải mái khi di chuyển hằng ngày."

        tvDetailPrice.text = "${currencyFormat.format(product.price)}đ"

        if (product.discountRate > 0) {
            tvDetailOriginalPrice.visibility = View.VISIBLE
            tvDetailDiscount.visibility = View.VISIBLE

            tvDetailOriginalPrice.text = "${currencyFormat.format(product.basePrice)}đ"
            tvDetailOriginalPrice.paintFlags = tvDetailOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val discountPercent = (product.discountRate * 100).toInt()
            tvDetailDiscount.text = "-${discountPercent}%"
        } else {
            tvDetailOriginalPrice.visibility = View.GONE
            tvDetailDiscount.visibility = View.GONE
        }

        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.banner_placeholder)
            .error(R.drawable.banner_placeholder)
            .into(imgDetailProduct)
    }
}