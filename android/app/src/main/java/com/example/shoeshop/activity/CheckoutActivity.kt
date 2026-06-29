package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoeshop.adapter.CheckoutItemAdapter
import com.example.shoeshop.databinding.CheckoutActivityBinding
import com.example.shoeshop.dto.request.CheckoutPreviewRequest
import com.example.shoeshop.dto.respone.CheckoutPreviewResponse
import com.example.shoeshop.retrofit.CheckoutRetrofit
import com.example.shoeshop.utils.PrefManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: CheckoutActivityBinding
    private lateinit var adapter: CheckoutItemAdapter
    private lateinit var selectedCartItemIds: ArrayList<Int>

    private var subtotal = 0.0
    private var shippingFee = 35000.0
    private var discount = 0.0
    private var paymentMethod = "COD"
    var userId = -1

    private val addressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loadCheckoutPreview()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerBar.tvTitle.text = "Thanh toán"
        binding.headerBar.btnBack.setOnClickListener { finish() }

        userId = PrefManager.getUser(this)?.id ?: -1
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnEditAddress.setOnClickListener {
            addressLauncher.launch(
                Intent(this, ShippingAddressActivity::class.java)
            )
        }

        selectedCartItemIds =
            intent.getIntegerArrayListExtra("SELECTED_CART_ITEM_IDS") ?: arrayListOf()
        Log.d("CHECKOUT", "Received IDs: $selectedCartItemIds (Size: ${selectedCartItemIds.size})")

        setupRecyclerView()
        setupShippingSelection()
        setupPaymentSelection()
        loadCheckoutPreview()

        binding.btnPlaceOrder.setOnClickListener {
            startActivity(Intent(this, OrderSuccessActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = CheckoutItemAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun loadCheckoutPreview() {
        lifecycleScope.launch {
            try {
                val response = CheckoutRetrofit.api.getCheckoutPreview(
                    userId,
                    CheckoutPreviewRequest(selectedCartItemIds)
                )
                if (response.isSuccessful) {
                    response.body()?.let { showCheckoutData(it) }
                } else {
                    Log.e(
                        "CHECKOUT",
                        "API Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("CHECKOUT", "API Exception", e)
            }
        }
    }

    private fun showCheckoutData(data: CheckoutPreviewResponse) {
        data.shippingAddress?.let {
            binding.tvReceiver.text = it.receiverName
            binding.tvPhone.text = it.phone
            binding.tvAddress.text = it.address
        }

        adapter.submitList(data.items)
        subtotal = data.items.sumOf { it.price * it.quantity }

        selectExpressShipping()
        selectCod()
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
        binding.tvShippingFee.text =
            if (shippingFee == 0.0) "Phí vận chuyển: Miễn phí" else "Phí vận chuyển: ${
                formatPrice(shippingFee)
            }"
        binding.tvDiscount.text = "Giảm giá: -${formatPrice(discount)}"
        binding.tvTotal.text = "Tổng cộng: ${formatPrice(total)}"
    }

    private fun formatPrice(price: Double): String {
        return NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price) + "đ"
    }
}