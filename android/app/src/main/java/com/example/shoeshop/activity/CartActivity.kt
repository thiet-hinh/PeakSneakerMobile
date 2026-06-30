package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.CartAdapter
import com.example.shoeshop.model.Cart
import com.example.shoeshop.api.ProductApi
import com.example.shoeshop.dto.respone.CartItemResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private var myCartList: List<Cart> = arrayListOf()
    private lateinit var cartAdapter: CartAdapter

    private lateinit var txtSubTotal: TextView
    private lateinit var txtTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var rvCartItems: RecyclerView
    private lateinit var layoutEmptyCart: LinearLayout

    // TODO: thay bằng userId thật của tài khoản đang đăng nhập (SharedPreferences/Session),
    // hiện đang giả lập tạm userId = 1 để test.
    private val userId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cart)

        // Ánh xạ các View từ XML
        txtSubTotal = findViewById(R.id.txtSubTotal)
        txtTotal = findViewById(R.id.txtTotal)
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart)
        rvCartItems = findViewById(R.id.rvCartItems)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        btnCheckout = findViewById<Button>(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            val selectedIds = myCartList.filter { it.isChecked }.map { it.cartItemId }

            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm để thanh toán", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putIntegerArrayListExtra("SELECTED_CART_ITEM_IDS", ArrayList(selectedIds))
            }
            startActivity(intent)
        }

        // Cấu hình Adapter và RecyclerView
        rvCartItems.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(
            itemList = myCartList,
            onTotalChanged = {
                updateTotalValue()
            },
            onQuantityChanged = { item, newQuantity, oldQuantity ->
                updateCartItemQuantityInDB(item, newQuantity, oldQuantity)
            },
            onRemoveItem = { item ->
                removeCartItem(item)
            }
        )
        rvCartItems.adapter = cartAdapter

        // Tải dữ liệu từ DB lúc mở màn hình lên
        loadCartDataFromDatabase()
    }

    private fun loadCartDataFromDatabase() {
        ProductApi.productApi.getCart(userId).enqueue(object : Callback<List<CartItemResponse>> {
            override fun onResponse(call: Call<List<CartItemResponse>>, response: Response<List<CartItemResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val dtoList = response.body()!!

                    myCartList = dtoList.map { dto ->
                        Cart(
                            cartItemId = dto.cartItemId,
                            productId = dto.productId,
                            variantId = dto.variantId,
                            name = dto.productName,
                            size = "Size: ${dto.size}",
                            color = dto.color,
                            price = dto.price,
                            quantity = dto.quantity,
                            stockQuantity = dto.stockQuantity,
                            image = dto.imageUrl,
                            isChecked = false
                        )
                    }

                    updateEmptyState()
                    cartAdapter.updateData(myCartList)
                    updateTotalValue()
                } else {
                    Toast.makeText(this@CartActivity, "Không thể tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CartItemResponse>>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Lỗi kết nối mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCartItemQuantityInDB(item: Cart, newQuantity: Int, oldQuantity: Int) {
        ProductApi.productApi.updateQuantity(item.cartItemId, newQuantity)
            .enqueue(object : Callback<CartItemResponse> {
                override fun onResponse(call: Call<CartItemResponse>, response: Response<CartItemResponse>) {
                    if (!response.isSuccessful) {
                        // Revert lại số lượng cũ trên UI (thường do vượt tồn kho)
                        item.quantity = oldQuantity
                        cartAdapter.refreshItem(item.cartItemId)
                        updateTotalValue()
                        Toast.makeText(
                            this@CartActivity,
                            "Không thể cập nhật số lượng (có thể đã vượt tồn kho)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CartItemResponse>, t: Throwable) {
                    item.quantity = oldQuantity
                    cartAdapter.refreshItem(item.cartItemId)
                    updateTotalValue()
                    Toast.makeText(this@CartActivity, "Lỗi mạng, không thể cập nhật DB!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun removeCartItem(item: Cart) {
        ProductApi.productApi.removeCartItem(item.cartItemId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    myCartList = myCartList.filter { it.cartItemId != item.cartItemId }
                    cartAdapter.updateData(myCartList)
                    updateTotalValue()
                    updateEmptyState()
                } else {
                    Toast.makeText(this@CartActivity, "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Lỗi mạng, không thể xóa sản phẩm!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateEmptyState() {
        if (myCartList.isEmpty()) {
            layoutEmptyCart.visibility = View.VISIBLE
            rvCartItems.visibility = View.GONE
        } else {
            layoutEmptyCart.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE
        }
    }

    private fun updateTotalValue() {
        var totalCost = 0.0

        for (item in myCartList) {
            if (item.isChecked) {
                totalCost += (item.price * item.quantity)
            }
        }

        val format = NumberFormat.getInstance(Locale("vi", "VN"))
        val formattedPrice = "${format.format(totalCost)}đ"

        txtSubTotal.text = formattedPrice
        txtTotal.text = formattedPrice
    }
}