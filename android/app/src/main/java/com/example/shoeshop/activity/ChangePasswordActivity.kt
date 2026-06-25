package com.example.shoeshop.activity

import android.R
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.shoeshop.databinding.ChangePasswordActivityBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ChangePasswordActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChangePasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.btnBack.setOnClickListener{
            finish()
        }
        binding.header.tvTitle.text =getString( com.example.shoeshop.R.string.titleChangePasswordPage)

        binding.edtNewPassword.doAfterTextChanged {

            val password = it.toString()

            binding.tvLength.setTextColor(
                if (password.length >= 8)
                    Color.parseColor("#396477")
                else
                    Color.parseColor("#747878")
            )

            binding.tvNumber.setTextColor(
                if (password.any { c -> c.isDigit() })
                    Color.parseColor("#396477")
                else
                    Color.parseColor("#747878")
            )

            binding.tvSpecial.setTextColor(
                if (password.matches(Regex(".*[!@#$%^&*()].*")))
                    Color.parseColor("#396477")
                else
                    Color.parseColor("#747878")
            )
        }

        binding.btnUpdatePassword.setOnClickListener {

            val oldPassword =
                binding.edtOldPassword.text.toString()

            val newPassword =
                binding.edtNewPassword.text.toString()

            val confirmPassword =
                binding.edtConfirmPassword.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(
                    this,
                    "Mật khẩu xác nhận không khớp",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Firebase re-authenticate
            // updatePassword(...)
        }
    }
}