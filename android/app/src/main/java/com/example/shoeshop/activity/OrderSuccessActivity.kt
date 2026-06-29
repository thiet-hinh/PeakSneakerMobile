package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.OrderSuccessActivityBinding

class OrderSuccessActivity : AppCompatActivity() {
    private lateinit var binding: OrderSuccessActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = OrderSuccessActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderCode = intent.getStringExtra("order_code") ?: "#PS-000001"

        binding.txtOrderCode.text = orderCode
        binding.txtPaymentMethod.text = "VNPAY"


        binding.btnContinueShopping.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("OPEN_FRAGMENT", "STORE")

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.btnOrderDetail.setOnClickListener {
            val intent = Intent(
                this,
                OrderDetailActivity::class.java
            )

            intent.putExtra("order_code", orderCode)

            startActivity(intent)
        }
    }
}