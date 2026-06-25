package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoeshop.adapter.OrderProductAdapter
import com.example.shoeshop.databinding.CheckoutActivityBinding
import com.example.shoeshop.dto.respone.OrderProduct
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: CheckoutActivityBinding
    private lateinit var adapter: OrderProductAdapter

    private var subtotal = 0.0
    private var shippingFee = 0.0
    private var discount = 250000.0
    private var paymentMethod = "VNPAY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerBar.tvTitle.text = "Thanh toán"
        binding.headerBar.btnBack.setOnClickListener { finish() }

        binding.btnEditAddress.setOnClickListener {
            val intent : Intent = Intent(this, ShippingAddressActivity:: class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        setupShippingSelection()
        setupPaymentSelection()

        loadData()

        binding.btnPlaceOrder.setOnClickListener {
            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
        }

        binding.btnPlaceOrder.setOnClickListener {
            val intent: Intent = Intent(this, OrderSuccessActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrderProductAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun loadData() {
        binding.tvReceiver.text = "Nguyễn Minh Anh"
        binding.tvPhone.text = "0901234567"
        binding.tvAddress.text = "Tầng 12, Bitexco, 2 Hải Triều, Bến Nghé, Quận 1, TP.HCM"

        val products = listOf(
            OrderProduct(imageUrl = "https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg", brand = "NIKE", productName = "Air Jordan 1", size = "42", color = "Đỏ", quantity = 1, price = 4250000.0),
            OrderProduct(imageUrl = "https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg", brand = "ADIDAS", productName = "Forum Low", size = "41", color = "Trắng", quantity = 1, price = 2850000.0)
        )

        adapter.submitList(products)
        subtotal = products.sumOf { it.price * it.quantity }

        selectStandardShipping()
        selectVnpay()
        updateTotal()
    }

    private fun setupShippingSelection() {
        binding.cardExpress.setOnClickListener { selectExpressShipping() }
        binding.cardStandard.setOnClickListener { selectStandardShipping() }
    }

    private fun setupPaymentSelection() {
        binding.cardCod.setOnClickListener { selectCod() }
        binding.cardVnpay.setOnClickListener { selectVnpay() }
    }

    private fun selectExpressShipping() {
        shippingFee = 35000.0
        binding.cardExpress.strokeWidth = 4
        binding.cardStandard.strokeWidth = 0
        binding.imgExpressCheck.visibility = View.VISIBLE
        binding.imgStandardCheck.visibility = View.GONE
        updateTotal()
    }

    private fun selectStandardShipping() {
        shippingFee = 0.0
        binding.cardExpress.strokeWidth = 0
        binding.cardStandard.strokeWidth = 4
        binding.imgExpressCheck.visibility = View.GONE
        binding.imgStandardCheck.visibility = View.VISIBLE
        updateTotal()
    }

    private fun selectCod() {
        paymentMethod = "COD"
        binding.cardCod.strokeWidth = 4
        binding.cardVnpay.strokeWidth = 0
        binding.imgCodSelected.visibility = View.VISIBLE
        binding.imgVnpaySelected.visibility = View.GONE
    }

    private fun selectVnpay() {
        paymentMethod = "VNPAY"
        binding.cardCod.strokeWidth = 0
        binding.cardVnpay.strokeWidth = 4
        binding.imgCodSelected.visibility = View.GONE
        binding.imgVnpaySelected.visibility = View.VISIBLE
    }

    private fun updateTotal() {
        val total = subtotal + shippingFee - discount

        binding.tvSubtotal.text = "Tạm tính: ${formatPrice(subtotal)}"
        binding.tvShippingFee.text = if (shippingFee == 0.0) "Phí vận chuyển: Miễn phí" else "Phí vận chuyển: ${formatPrice(shippingFee)}"
        binding.tvDiscount.text = "Giảm giá: -${formatPrice(discount)}"
        binding.tvTotal.text = "Tổng cộng: ${formatPrice(total)}"
    }

    private fun formatPrice(price: Double): String {
        return NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price) + "đ"
    }
}