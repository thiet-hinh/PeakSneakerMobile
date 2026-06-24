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

    // Danh sách sản phẩm của bạn
    private lateinit var myCartList: List<Cart>

    private lateinit var txtSubTotal: TextView
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cart)

        // Ánh xạ View
        txtSubTotal = findViewById(R.id.txtSubTotal)
        txtTotal = findViewById(R.id.txtTotal)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val rvCartItems = findViewById<RecyclerView>(R.id.rvCartItems)

        // Nút Back
        btnBack.setOnClickListener { finish() }

        // Dữ liệu mẫu (Nhập thẳng số thay vì text)
        myCartList = listOf(
            Cart("Nike Air Max 270", "Size: 42", 3290000L, 1),
            Cart("Vans Old Skool Black", "Size: 41", 1850000L, 1)
        )

        // Cài đặt RecyclerView
        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.adapter = CartAdapter(myCartList) {
            // Callback này sẽ chạy mỗi khi bạn tick checkbox hoặc thay đổi số lượng
            updateTotalValue()
        }

        // Tính tổng lần đầu tiên khi vừa vào màn hình
        updateTotalValue()
    }

    // Hàm tính toán và hiển thị tổng tiền
    private fun updateTotalValue() {
        var totalCost = 0L

        // Duyệt qua danh sách, chỉ cộng tiền những item đang được tick (isChecked = true)
        for (item in myCartList) {
            if (item.isChecked) {
                totalCost += (item.price * item.quantity)
            }
        }

        // Format lại ra dạng tiền tệ VNĐ
        val format = NumberFormat.getInstance(Locale("vi", "VN"))
        val formattedPrice = "${format.format(totalCost)}đ"

        // Cập nhật lên UI
        txtSubTotal.text = formattedPrice
        txtTotal.text = formattedPrice
    }
}