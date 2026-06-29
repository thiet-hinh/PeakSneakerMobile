package com.example.shoeshop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.R
import com.example.shoeshop.databinding.ChangePasswordActivityBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ChangePasswordActivityBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangePasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.btnBack.setOnClickListener { finish() }
        binding.header.tvTitle.text = getString(R.string.titleChangePasswordPage)
        binding.btnUpdatePassword.setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        val oldPassword = binding.edtOldPassword.text.toString().trim()
        val newPassword = binding.edtNewPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        when {
            oldPassword.isEmpty() -> {
                binding.edtOldPassword.error = "Vui lòng nhập mật khẩu cũ"
                binding.edtOldPassword.requestFocus()
                return
            }
            newPassword.isEmpty() -> {
                binding.edtNewPassword.error = "Vui lòng nhập mật khẩu mới"
                binding.edtNewPassword.requestFocus()
                return
            }
            newPassword.length < 6 -> {
                binding.edtNewPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
                binding.edtNewPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                binding.edtConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
                binding.edtConfirmPassword.requestFocus()
                return
            }
            newPassword != confirmPassword -> {
                binding.edtConfirmPassword.error = "Mật khẩu xác nhận không khớp"
                binding.edtConfirmPassword.requestFocus()
                return
            }
            oldPassword == newPassword -> {
                binding.edtNewPassword.error = "Mật khẩu mới phải khác mật khẩu cũ"
                binding.edtNewPassword.requestFocus()
                return
            }
        }

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show()
            return
        }

        val email = user.email
        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "Không tìm thấy email tài khoản", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnUpdatePassword.isEnabled = false
        val credential = EmailAuthProvider.getCredential(email, oldPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        binding.btnUpdatePassword.isEnabled = true
                        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        binding.btnUpdatePassword.isEnabled = true
                        Toast.makeText(this, e.message ?: "Không thể đổi mật khẩu", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                binding.btnUpdatePassword.isEnabled = true
                binding.edtOldPassword.error = "Mật khẩu cũ không đúng"
                binding.edtOldPassword.requestFocus()
            }
    }
}