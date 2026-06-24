package com.example.shoeshop.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.adapter.AddressSpinnerAdapter
import com.example.shoeshop.databinding.ShippingAddressActivityBinding
import com.example.shoeshop.dto.respone.AddressItem

class ShippingAddressActivity : AppCompatActivity() {

    private lateinit var binding: ShippingAddressActivityBinding

    private var selectedProvince: AddressItem? = null
    private var selectedDistrict: AddressItem? = null
    private var selectedWard: AddressItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShippingAddressActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        loadProvinces()
    }

    private fun initListeners() {
        binding.spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedProvince = parent?.getItemAtPosition(position) as AddressItem
                loadDistricts(selectedProvince!!.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDistrict = parent?.getItemAtPosition(position) as AddressItem
                loadWards(selectedDistrict!!.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWard = parent?.getItemAtPosition(position) as AddressItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnSave.setOnClickListener {
            val provinceId = selectedProvince?.id ?: return@setOnClickListener
            val districtId = selectedDistrict?.id ?: return@setOnClickListener
            val wardCode = selectedWard?.id ?: return@setOnClickListener
            val detailAddress = binding.edtDetail.text.toString()

            Log.d("ADDRESS", "ProvinceId = $provinceId\nDistrictId = $districtId\nWardCode = $wardCode\nDetail = $detailAddress")
        }
    }

    private fun loadProvinces() {
        val provinces = listOf(
            AddressItem("202", "Hồ Chí Minh"),
            AddressItem("201", "Hà Nội"),
            AddressItem("203", "Đà Nẵng")
        )
        binding.spProvince.adapter = AddressSpinnerAdapter(this, provinces)
    }

    private fun loadDistricts(provinceId: String) {
        val districts = when (provinceId) {
            "202" -> listOf(
                AddressItem("1442", "Quận Bình Thạnh"),
                AddressItem("1443", "Quận 1"),
                AddressItem("1444", "Quận 3")
            )
            else -> emptyList()
        }
        binding.spDistrict.adapter = AddressSpinnerAdapter(this, districts)
    }

    private fun loadWards(districtId: String) {
        val wards = when (districtId) {
            "1442" -> listOf(
                AddressItem("20308", "Phường 25"),
                AddressItem("20309", "Phường 26")
            )
            else -> emptyList()
        }
        binding.spWard.adapter = AddressSpinnerAdapter(this, wards)
    }
}