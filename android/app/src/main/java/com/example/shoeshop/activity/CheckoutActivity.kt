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
import com.example.shoeshop.dto.request.ApplyVoucherRequest
import com.example.shoeshop.dto.request.CheckoutPreviewRequest
import com.example.shoeshop.dto.request.PlaceOrderRequest
import com.example.shoeshop.dto.respone.CheckoutPreviewResponse
import com.example.shoeshop.dto.respone.PlaceOrderResponse
import com.example.shoeshop.enums.DeliveryMethod
import com.example.shoeshop.enums.PaymentMethod
import com.example.shoeshop.retrofit.CheckoutRetrofit
import com.example.shoeshop.retrofit.OrderRetrofit
import com.example.shoeshop.retrofit.VoucherRetrofit
import com.example.shoeshop.utils.PrefManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: CheckoutActivityBinding
    private lateinit var adapter: CheckoutItemAdapter
    private lateinit var selectedCartItemIds: ArrayList<Int>

    private var subtotal = 0.0
    private var shippingFee = 35000.0
    private var discount = 0.0

    private var finalAmount =0.0

    private var voucherCode: String =""
    private var paymentMethod = PaymentMethod.COD

    private var deliveryMethod = DeliveryMethod.SAVE
    private var userId = -1

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
        setupVoucher()
        setupPlaceOrder()
    }

    private fun setupPlaceOrder() {
        binding.btnPlaceOrder.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        OrderRetrofit.api.placeOrder(
                            PlaceOrderRequest(
                                userId = userId,
                                selectedCartItemIds = selectedCartItemIds,
                                voucherCode = voucherCode,
                                paymentMethod = paymentMethod,
                                deliveryMethod = deliveryMethod
                            )
                        )
                    }

                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            startActivity(Intent(this@CheckoutActivity, OrderSuccessActivity::class.java).apply {
                                putExtra("order_code", body.orderCode)
                                putExtra("payment_method", body.paymentMethod)
                                putExtra("estimated_delivery_date", body.estimatedDeliveryDate)
                            })
                        }
                    } else {
                        try {
                            val errorBody = response.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, PlaceOrderResponse::class.java)
                            Toast.makeText(this@CheckoutActivity, errorResponse.message, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@CheckoutActivity, "Đặt hàng thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ORDER", "Place order error", e)
                }
            }
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
        updateFinalAmount()
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
        deliveryMethod= DeliveryMethod.FAST
        binding.cardExpress.strokeWidth = 4
        binding.cardStandard.strokeWidth = 0
        binding.imgExpressCheck.visibility = View.VISIBLE
        binding.imgStandardCheck.visibility = View.GONE
        updateFinalAmount()
    }

    private fun selectStandardShipping() {
        shippingFee = 0.0
        deliveryMethod= DeliveryMethod.SAVE
        binding.cardExpress.strokeWidth = 0
        binding.cardStandard.strokeWidth = 4
        binding.imgExpressCheck.visibility = View.GONE
        binding.imgStandardCheck.visibility = View.VISIBLE
        updateFinalAmount()
    }

    private fun selectCod() {
        paymentMethod = PaymentMethod.COD
        binding.cardCod.strokeWidth = 4
        binding.cardVnpay.strokeWidth = 0
        binding.imgCodSelected.visibility = View.VISIBLE
        binding.imgVnpaySelected.visibility = View.GONE
    }

    private fun selectVnpay() {
        paymentMethod = PaymentMethod.VNPAY
        binding.cardCod.strokeWidth = 0
        binding.cardVnpay.strokeWidth = 4
        binding.imgCodSelected.visibility = View.GONE
        binding.imgVnpaySelected.visibility = View.VISIBLE
    }

    private fun updateFinalAmount() {
        finalAmount = subtotal + shippingFee - discount
        binding.tvSubtotal.text = "Tạm tính: ${formatPrice(subtotal)}"
        binding.tvShippingFee.text =
            if (shippingFee == 0.0) "Phí vận chuyển: Miễn phí" else "Phí vận chuyển: ${
                formatPrice(shippingFee)
            }"
        binding.tvDiscount.text = "Giảm giá: -${formatPrice(discount)}"
        binding.tvTotal.text = "Tổng cộng: ${formatPrice(finalAmount)}"
    }

    private fun formatPrice(price: Double): String {
        return NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price) + "đ"
    }

    private fun setupVoucher() {
        binding.btnApplyVoucher.setOnClickListener {
            val code = binding.edtVoucher.text.toString().trim()
            if (code.isBlank()) {
                showMessage("Vui lòng nhập mã giảm giá")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = VoucherRetrofit.api.applyVoucher(
                        ApplyVoucherRequest(userId = userId, orderAmount = subtotal, voucherCode = code)
                    )

                    if (!response.isSuccessful) {
                        showMessage("Không thể kết nối máy chủ")
                        return@launch
                    }

                    val body = response.body()
                    if (body == null) {
                        showMessage("Không nhận được dữ liệu")
                        return@launch
                    }

                    if (body.success) {
                        discount = body.discountAmount
                        voucherCode = body.code!!
                    } else {
                        discount = 0.0
                        voucherCode = ""
                    }
                    updateFinalAmount()
                    showMessage(body.message)
                } catch (e: Exception) {
                    Log.e("Voucher", "Apply voucher failed", e)
                    discount = 0.0
                    voucherCode = ""
                    updateFinalAmount()
                    showMessage(e.message ?: "Đã xảy ra lỗi")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        try {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Toast", "Cannot show toast", e)
        }
    }

}