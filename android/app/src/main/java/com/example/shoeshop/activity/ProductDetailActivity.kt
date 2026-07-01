package com.example.shoeshop.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoeshop.R
import com.example.shoeshop.model.CartRequest
import com.example.shoeshop.model.Product
import com.example.shoeshop.model.ProductVariant
import com.example.shoeshop.retrofit.ProductRetrofit
import com.example.shoeshop.utils.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgDetailProduct: ImageView
    private lateinit var tvDetailBrand: TextView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var tvDetailOriginalPrice: TextView
    private lateinit var tvDetailDiscount: TextView
    private lateinit var tvDetailRating: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var layoutColors: LinearLayout
    private lateinit var gridSizes: GridLayout
    private lateinit var btnAddToCart: LinearLayout
    private lateinit var btnBack: ImageView

    private var productId: Int = -1
    private var currentUserId: Int = -1
    private var allVariants: List<ProductVariant> = listOf()

    private var selectedColor: String? = null
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Lấy user hiện tại từ PrefManager
        val currentUser = PrefManager.getUser(this)
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        currentUserId = currentUser.id

        productId = intent.getIntExtra("PRODUCT_ID", 1)

        initViews()
        loadProductDetail()

        btnBack.setOnClickListener { finish() }
        btnAddToCart.setOnClickListener { handleAddToCart() }
    }

    private fun initViews() {
        imgDetailProduct = findViewById(R.id.imgDetailProduct)
        tvDetailBrand = findViewById(R.id.tvDetailBrand)
        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailPrice = findViewById(R.id.tvDetailPrice)
        tvDetailOriginalPrice = findViewById(R.id.tvDetailOriginalPrice)
        tvDetailDiscount = findViewById(R.id.tvDetailDiscount)
        tvDetailRating = findViewById(R.id.tvDetailRating)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)
        layoutColors = findViewById(R.id.layoutColors)
        gridSizes = findViewById(R.id.gridSizes)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun loadProductDetail() {
        ProductRetrofit.api.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful && response.body() != null) {
                    val product = response.body()!!
                    allVariants = product.variants ?: listOf()

                    tvDetailBrand.text = product.brandName.uppercase()
                    tvDetailName.text = product.name
                    tvDetailRating.text = product.rating
                    tvDetailPrice.text = String.format("%,.0fđ", product.price)
                    tvDetailOriginalPrice.text = String.format("%,.0fđ", product.basePrice)
                    tvDetailDiscount.text = String.format("-%.0f%%", product.discount)
                    tvDetailDescription.text = product.description ?: "Không có mô tả cho sản phẩm này."

                    Glide.with(this@ProductDetailActivity)
                        .load(product.imageUrl)
                        .placeholder(R.drawable.banner_placeholder)
                        .into(imgDetailProduct)

                    val uniqueColors = allVariants.mapNotNull { it.color }.distinct()
                    renderColors(uniqueColors)

                    val uniqueSizes = allVariants
                        .filter { it.stockQuantity > 0 }
                        .mapNotNull { it.size }
                        .distinct()
                    renderSizes(uniqueSizes)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Không thể kết nối đến máy chủ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun renderColors(colors: List<String>) {
        layoutColors.removeAllViews()
        colors.forEach { colorName ->
            val textView = TextView(this)
            val heightInDp = (40 * resources.displayMetrics.density).toInt()
            val marginInDp = (12 * resources.displayMetrics.density).toInt()

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, heightInDp)
            params.setMargins(0, 0, marginInDp, 0)
            textView.layoutParams = params
            textView.text = colorName
            textView.setPadding(32, 0, 32, 0)
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.bg_size_unselected)
            textView.setTextColor(resources.getColor(android.R.color.black, null))

            textView.setOnClickListener {
                selectedColor = colorName
                selectedSize = null

                for (i in 0 until layoutColors.childCount) {
                    val child = layoutColors.getChildAt(i) as TextView
                    child.setBackgroundResource(R.drawable.bg_size_unselected)
                    child.setTextColor(resources.getColor(android.R.color.black, null))
                }
                textView.setBackgroundResource(R.drawable.bg_size_selected)
                textView.setTextColor(resources.getColor(android.R.color.white, null))

                val availableSizes = allVariants
                    .filter { it.color == selectedColor && it.stockQuantity > 0 }
                    .mapNotNull { it.size }
                    .distinct()
                renderSizes(availableSizes)
            }
            layoutColors.addView(textView)
        }
    }

    private fun renderSizes(sizes: List<String>) {
        gridSizes.removeAllViews()
        sizes.forEach { sizeValue ->
            val textView = TextView(this)
            val widthInDp = (64 * resources.displayMetrics.density).toInt()
            val heightInDp = (40 * resources.displayMetrics.density).toInt()
            val marginInDp = (8 * resources.displayMetrics.density).toInt()

            val params = GridLayout.LayoutParams()
            params.width = widthInDp
            params.height = heightInDp
            params.setMargins(0, 0, marginInDp, marginInDp)
            textView.layoutParams = params
            textView.text = sizeValue
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.bg_size_unselected)
            textView.setTextColor(resources.getColor(android.R.color.black, null))

            textView.setOnClickListener {
                selectedSize = sizeValue

                for (i in 0 until gridSizes.childCount) {
                    val child = gridSizes.getChildAt(i) as TextView
                    child.setBackgroundResource(R.drawable.bg_size_unselected)
                    child.setTextColor(resources.getColor(android.R.color.black, null))
                }
                textView.setBackgroundResource(R.drawable.bg_size_selected)
                textView.setTextColor(resources.getColor(android.R.color.white, null))
            }
            gridSizes.addView(textView)
        }
    }

    private fun handleAddToCart() {
        if (selectedColor == null) {
            Toast.makeText(this, "Vui lòng chọn Màu Sắc sản phẩm!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedSize == null) {
            Toast.makeText(this, "Vui lòng chọn Kích Cỡ sản phẩm!", Toast.LENGTH_SHORT).show()
            return
        }

        val cartRequest = CartRequest(
            userId = currentUserId,
            productId = productId,
            color = selectedColor!!,
            size = selectedSize!!
        )

        ProductRetrofit.api.addToCart(cartRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Đã thêm [$selectedColor - Size $selectedSize] vào giỏ hàng!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Thêm vào giỏ hàng thất bại!"
                    Toast.makeText(this@ProductDetailActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}