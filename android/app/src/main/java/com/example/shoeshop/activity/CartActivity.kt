package com.example.shoeshop.activity // Đổi theo package của bạn

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cart)

        txtSubTotal = findViewById(R.id.txtSubTotal)
        txtTotal = findViewById(R.id.txtTotal)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val rvCartItems = findViewById<RecyclerView>(R.id.rvCartItems)

        btnBack.setOnClickListener { finish() }

        myCartList = listOf(
            Cart("Nike Air Max 270", "Size: 42", 3290000L, 1),
            Cart("Vans Old Skool Black", "Size: 41", 1850000L, 1)
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