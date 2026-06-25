package com.example.shoeshop.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.EditProfileActivityBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: EditProfileActivityBinding

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.imgAvatar.setImageURI(it)
                // TODO: Upload avatar lên server ở đây
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EditProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvTitle.text = "Chỉnh Sửa Thông Tin"
        binding.header.btnBack.setOnClickListener {
            finish()
        }

        binding.imgAvatar.setOnClickListener {
            openGallery()
        }

        binding.btnSave.setOnClickListener {

            val lastName = binding.edtLastName.text.toString()
            val firstName = binding.edtFirstName.text.toString()
            val phone = binding.edtPhone.text.toString()

            // call api update profile
        }

    }

    fun openGallery() {
        pickImageLauncher.launch("image/*")
    }
}