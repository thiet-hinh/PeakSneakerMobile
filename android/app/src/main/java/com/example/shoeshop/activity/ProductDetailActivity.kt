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
    private var allVariants: List<ProductVariant> = listOf()

    // Lưu thuộc tính được lựa chọn để gửi lên Server
    private var selectedColor: String? = null
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Nhận ID sản phẩm từ Intent khi click ở màn hình danh sách Store
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

                    // Sử dụng các Getter thông minh có sẵn trong Model của bạn
                    tvDetailBrand.text = product.brandName.uppercase()
                    tvDetailName.text = product.name
                    tvDetailRating.text = product.rating
                    tvDetailPrice.text = String.format("%,.0fđ", product.price)
                    tvDetailOriginalPrice.text = String.format("%,.0fđ", product.basePrice)
                    tvDetailDiscount.text = String.format("-%.0f%%", product.discount)

                    // 💡 Đã hoạt động mượt mà vì biến product đã có trường description
                    tvDetailDescription.text = product.description ?: "Không có mô tả cho sản phẩm này."

                    Glide.with(this@ProductDetailActivity)
                        .load(product.imageUrl)
                        .placeholder(R.drawable.banner_placeholder)
                        .into(imgDetailProduct)

                    // Phân tách lấy danh sách các Tên Màu duy nhất từ danh sách biến thể
                    val uniqueColors = allVariants.mapNotNull { it.color }.distinct()
                    renderColors(uniqueColors)

                    // Lúc đầu chưa chọn màu cụ thể -> Hiển thị các size còn hàng nói chung
                    val uniqueSizes = allVariants.filter { it.stockQuantity > 0 }.mapNotNull { it.size }.distinct()
                    renderSizes(uniqueSizes)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Không thể kết nối đến máy chủ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Sinh các thẻ chọn màu sắc dựa trên tên chữ từ Database
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
            textView.setPadding(32, 0, 32, 0) // Tạo khoảng cách đệm lề cho chữ dài (vd: "Black/White Check")
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.bg_size_unselected)
            textView.setTextColor(resources.getColor(android.R.color.black, null))

            textView.setOnClickListener {
                selectedColor = colorName
                selectedSize = null // Reset size đã chọn trước đó tránh xung đột cấu hình kho

                // Cập nhật trạng thái hiển thị của các Chip màu sắc
                for (i in 0 until layoutColors.childCount) {
                    val child = layoutColors.getChildAt(i) as TextView
                    child.setBackgroundResource(R.drawable.bg_size_unselected)
                    child.setTextColor(resources.getColor(android.R.color.black, null))
                }
                textView.setBackgroundResource(R.drawable.bg_size_selected)
                textView.setTextColor(resources.getColor(android.R.color.white, null))

                // 🔥 LỌC THÔNG MINH: Chỉ hiển thị những Size CÒN HÀNG tương ứng với màu này
                val availableSizes = allVariants
                    .filter { it.color == selectedColor && it.stockQuantity > 0 }
                    .mapNotNull { it.size }
                    .distinct()

                renderSizes(availableSizes)
            }
            layoutColors.addView(textView)
        }
    }

    // Sinh lưới chọn kích thước động
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

                // Cập nhật trạng thái hiển thị của lưới kích cỡ
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

        // Tạo gói Request truyền tải dữ liệu
        val cartRequest = CartRequest(
            userId = 1, // Thay bằng ID người dùng thực tế lấy từ Session Đăng Nhập
            productId = productId,
            color = selectedColor!!,
            size = selectedSize!!
        )

        ProductRetrofit.api.addToCart(cartRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductDetailActivity, "Đã thêm [$selectedColor - Size $selectedSize] vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}