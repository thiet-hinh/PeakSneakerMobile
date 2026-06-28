package com.example.shoeshop.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoeshop.R
import com.example.shoeshop.adapter.OrderProductAdapter
import com.example.shoeshop.databinding.OrderDetailActivityBinding
import com.example.shoeshop.dto.respone.OrderDetailResponse
import com.example.shoeshop.retrofit.OrderRetrofit
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: OrderDetailActivityBinding
    private lateinit var adapter: OrderProductAdapter
    private lateinit var order: OrderDetailResponse
    private var orderId = -1
    private var orderChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = OrderDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<TextView>(R.id.tvTitle).text =
            intent.getStringExtra("title") ?: "Chi tiết đơn hàng"

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        orderId = intent.getStringExtra("ORDER_ID")!!.toInt()

        adapter = OrderProductAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val response = OrderRetrofit.api.getOrderDetail(orderId)
                order = response

                binding.tvSubtotal.text = formatPrice(order.subtotal)
                binding.tvShippingFee.text = if (order.shippingFee == 0.0) "Phí vận chuyển: Miễn phí" else "Phí vận chuyển: ${formatPrice(order.shippingFee)}"
                binding.tvAddress.text = order.shippingAddress
                binding.tvTotal.text = formatPrice(order.finalAmount)
                binding.tvDiscount.text = if(order.discountAmount>0) formatPrice(order.discountAmount) else "Không có"
                adapter.submitList(order.items)
                showCancelButton()
            } catch (e: Exception) {
                Toast.makeText(this@OrderDetailActivity, "Không tải được đơn hàng hoặc lỗi kết nối", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCancelButton() {
        if (order.status == "PROCESSING") {
            binding.btnCancel.visibility = View.VISIBLE
            binding.btnCancel.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Hủy đơn hàng")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                    .setPositiveButton("Hủy đơn") { _, _ -> cancelOrder() }
                    .setNegativeButton("Đóng", null)
                    .show()
            }
        } else {
            binding.btnCancel.visibility = View.GONE
        }
    }

    private fun cancelOrder() {
        lifecycleScope.launch {
            try {
                OrderRetrofit.api.updateOrderStatus(orderId, "CANCELLED")
                orderChanged = true
                Toast.makeText(this@OrderDetailActivity, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show()
                loadData()
            } catch (e: Exception) {
                Toast.makeText(this@OrderDetailActivity, "Hủy đơn thất bại hoặc lỗi kết nối", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        if (orderChanged) {
            setResult(Activity.RESULT_OK)
        }
        super.finish()
    }

    private fun formatPrice(price: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(price)
    }
}