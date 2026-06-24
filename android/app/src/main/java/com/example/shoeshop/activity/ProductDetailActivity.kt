package com.example.shoeshop.activity // Giữ nguyên package của bạn nhé

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.R // Giữ nguyên phần import R của bạn

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // 1. Ánh xạ (tìm) cái nút Back thông qua ID mình vừa đặt bên XML
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // 2. Lắng nghe sự kiện click
        btnBack.setOnClickListener {
            // Lệnh kết thúc Activity hiện tại, tự động quay về màn hình trước
            finish()
        }
    }
}