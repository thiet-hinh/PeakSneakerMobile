package com.example.shoeshop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.EditProfileActivityBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: EditProfileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EditProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvTitle.text = "Chỉnh Sửa Thông Tin"
        binding.header.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {

            val lastName = binding.edtLastName.text.toString()
            val firstName = binding.edtFirstName.text.toString()
            val email = binding.edtEmail.text.toString()
            val phone = binding.edtPhone.text.toString()

            // call api update profile
        }

    }
}