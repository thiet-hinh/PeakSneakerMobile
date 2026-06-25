package com.example.shoeshop.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoeshop.R
import com.example.shoeshop.adapter.OrderProductAdapter
import com.example.shoeshop.databinding.OrderDetailActivityBinding
import com.example.shoeshop.dto.respone.OrderProduct
import java.text.NumberFormat
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: OrderDetailActivityBinding
    private lateinit var adapter: OrderProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = OrderDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<TextView>(R.id.tvTitle).text = intent.getStringExtra("title") ?: "Chi tiết đơn hàng"

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        adapter = OrderProductAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        loadData()
    }

    private fun loadData() {
        val shippingFee = 0.0

        binding.tvShippingFee.text =
            if (shippingFee == 0.0)
                "Phí vận chuyển: Miễn phí"
            else
                "Phí vận chuyển: ${formatPrice(shippingFee)}"
        binding.tvAddress.text ="Tổ 5 ấp 7 xã Suối Dây huyện Tân Châu tỉnh Tây Ninh"

        val products = listOf(
            OrderProduct(
                imageUrl = "https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg",
                brand = "NIKE",
                productName = "Air Max 270 Red",
                size = "42",
                color = "Đỏ",
                quantity = 1,
                price = 3250000.0
            ),
            OrderProduct(
                imageUrl = "https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg",
                brand = "NIKE",
                productName = "Air Force 1 White",
                size = "41",
                color = "Trắng",
                quantity = 1,
                price = 1890000.0
            )
        )

        adapter.submitList(products)
    }

    private fun formatPrice(price: Double): String =
        NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(price)
}