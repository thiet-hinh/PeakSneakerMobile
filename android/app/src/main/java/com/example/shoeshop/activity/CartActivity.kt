package com.example.shoeshop.activity // Đổi theo package của bạn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.CartAdapter
import com.example.shoeshop.model.Cart
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var myCartList: List<Cart>

    private lateinit var txtSubTotal: TextView
    private lateinit var txtTotal: TextView

    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cart)

        txtSubTotal = findViewById(R.id.txtSubTotal)
        txtTotal = findViewById(R.id.txtTotal)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val rvCartItems = findViewById<RecyclerView>(R.id.rvCartItems)

        btnBack.setOnClickListener { finish() }


        btnCheckout= findViewById<Button>(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            // 1. Lọc lấy danh sách ID của các item đã được tick chọn
            val selectedIds = myCartList.filter { it.isChecked }.map { it.id }

            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm để thanh toán", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putIntegerArrayListExtra("SELECTED_CART_ITEM_IDS", ArrayList(selectedIds))
            }
            startActivity(intent)
        }

        // Nhớ call danh sách có id bằng API trả về DTO (CartItemResponse)
        myCartList = listOf(
            Cart(1,"Nike Air Max 270", "Size: 42", 3290000L, 1),
            Cart(2,"Vans Old Skool Black", "Size: 41", 1850000L, 1)
        )

        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.adapter = CartAdapter(myCartList) {
            updateTotalValue()
        }

        updateTotalValue()
    }

    private fun updateTotalValue() {
        var totalCost = 0L

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