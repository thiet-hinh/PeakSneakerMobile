package com.example.shoeshop.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.R
import com.example.shoeshop.adapter.AddressSpinnerAdapter
import com.example.shoeshop.databinding.ShippingAddressActivityBinding
import com.example.shoeshop.dto.request.AddressRequest
import com.example.shoeshop.dto.respone.AddressItem
import com.example.shoeshop.retrofit.AddressRetrofit
import com.example.shoeshop.utils.PrefManager
import kotlinx.coroutines.launch

class ShippingAddressActivity : AppCompatActivity() {

    private lateinit var binding: ShippingAddressActivityBinding
    private var selectedProvince: AddressItem? = null
    private var selectedDistrict: AddressItem? = null
    private var selectedWard: AddressItem? = null
    private var restoringAddress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShippingAddressActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvTitle.text = getString(R.string.titleEditAdressPage)
        binding.header.btnBack.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveAddress() }

        initListeners()
        loadDefaultAddress()
    }

    private fun initListeners() {
        binding.spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedProvince = parent?.getItemAtPosition(position) as? AddressItem
                if (!restoringAddress) {
                    selectedDistrict = null
                    selectedWard = null
                    binding.spDistrict.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, emptyList())
                    binding.spWard.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, emptyList())
                    selectedProvince?.let { loadDistricts(it.id.toInt()) }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDistrict = parent?.getItemAtPosition(position) as? AddressItem
                if (!restoringAddress) {
                    selectedWard = null
                    binding.spWard.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, emptyList())
                    selectedDistrict?.let { loadWards(it.id.toInt()) }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWard = parent?.getItemAtPosition(position) as? AddressItem
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadProvinces() {
        lifecycleScope.launch {
            val provinces = getProvinces()
            binding.spProvince.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, provinces)
        }
    }

    private fun loadDistricts(provinceId: Int) {
        lifecycleScope.launch {
            val districts = getDistricts(provinceId)
            binding.spDistrict.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, districts)
        }
    }

    private fun loadWards(districtId: Int) {
        lifecycleScope.launch {
            val wards = getWards(districtId)
            binding.spWard.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, wards)
        }
    }

    private suspend fun getProvinces(): List<AddressItem> =
        AddressRetrofit.api.getProvinces().body() ?: emptyList()

    private suspend fun getDistricts(provinceId: Int): List<AddressItem> =
        AddressRetrofit.api.getDistricts(provinceId).body() ?: emptyList()

    private suspend fun getWards(districtId: Int): List<AddressItem> =
        AddressRetrofit.api.getWards(districtId).body() ?: emptyList()

    private fun loadDefaultAddress() {
        lifecycleScope.launch {
            try {
                val userId = PrefManager.getUser(this@ShippingAddressActivity)?.id ?: return@launch
                val response = AddressRetrofit.api.getDefaultAddress(userId.toString())

                if (!response.isSuccessful || response.body() == null) {
                    loadProvinces()
                    return@launch
                }

                restoringAddress = true
                val address = response.body()!!

                binding.edtFullName.setText(address.userName)
                binding.edtPhone.setText(address.phone)
                binding.edtDetail.setText(address.streetDetail)

                val provinces = getProvinces()
                binding.spProvince.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, provinces)
                binding.spProvince.setSelectionById(address.provinceId.toString())
                selectedProvince = provinces.firstOrNull { it.id == address.provinceId.toString() }

                val districts = getDistricts(address.provinceId)
                binding.spDistrict.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, districts)
                binding.spDistrict.setSelectionById(address.districtId.toString())
                selectedDistrict = districts.firstOrNull { it.id == address.districtId.toString() }

                val wards = getWards(address.districtId)
                binding.spWard.adapter = AddressSpinnerAdapter(this@ShippingAddressActivity, wards)
                binding.spWard.setSelectionById(address.wardId.toString())
                selectedWard = wards.firstOrNull { it.id == address.wardId.toString() }

            } catch (e: Exception) {
                Toast.makeText(this@ShippingAddressActivity, e.localizedMessage ?: "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                loadProvinces()
            } finally {
                restoringAddress = false
            }
        }
    }

    private fun saveAddress() {
        val province = selectedProvince ?: return
        val district = selectedDistrict ?: return
        val ward = selectedWard ?: return

        val userName = binding.edtFullName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val detail = binding.edtDetail.text.toString().trim()

        if (userName.isEmpty() || phone.isEmpty() || detail.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AddressRequest(
            provinceId = province.id.toInt(),
            provinceName = province.name,
            districtId = district.id.toInt(),
            districtName = district.name,
            wardId = ward.id.toInt(),
            wardName = ward.name,
            streetDetail = detail,
            userName = userName,
            phone = phone
        )

        lifecycleScope.launch {
            val userId = PrefManager.getUser(this@ShippingAddressActivity)?.id ?: return@launch
            val response = AddressRetrofit.api.updateDefaultAddress(userId, request)

            if (response.isSuccessful) {
                Toast.makeText(this@ShippingAddressActivity, "Lưu địa chỉ thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@ShippingAddressActivity, "Không thể lưu địa chỉ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun Spinner.setSelectionById(id: String) {
        val adapter = adapter as? AddressSpinnerAdapter ?: return
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i)?.id == id) {
                setSelection(i, false)
                return
            }
        }
    }
}