package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.OrderSuccessActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OrderSuccessActivity : AppCompatActivity() {
    private lateinit var binding: OrderSuccessActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderSuccessActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_code")
        val orderCode = "#DH0000${intent.getStringExtra("order_code") ?: "0"}"
        val paymentMethod = intent.getStringExtra("payment_method") ?: "COD"
        val rawDate = intent.getStringExtra("estimated_delivery_date") ?: ""

        val formattedDate = try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = originalFormat.parse(rawDate)
            date?.let { targetFormat.format(it) } ?: rawDate
        } catch (e: Exception) {
            rawDate
        }

        binding.txtOrderCode.text = orderCode
        binding.txtPaymentMethod.text = paymentMethod
        binding.txtDeliveryDate.text = formattedDate

        binding.btnContinueShopping.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OPEN_FRAGMENT", "STORE")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        binding.btnOrderDetail.setOnClickListener {
            startActivity(Intent(this, OrderDetailActivity::class.java).putExtra("ORDER_ID", orderId))
        }
    }
}