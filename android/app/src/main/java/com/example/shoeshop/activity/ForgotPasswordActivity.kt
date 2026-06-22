package com.example.shoeshop.activity


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.ForgotPasswordActivityBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupViews()
    }

    private fun setupViews() {
        binding.btnConfirm.setOnClickListener { sendResetPasswordEmail() }

        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun sendResetPasswordEmail() {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.edtEmail.error = "Vui lòng nhập email"
            binding.edtEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Email không hợp lệ"
            binding.edtEmail.requestFocus()
            return
        }

        binding.btnConfirm.isEnabled = false
        binding.btnConfirm.text = "Đang gửi..."

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            binding.btnConfirm.isEnabled = true
            binding.btnConfirm.text = "Xác nhận email"

            if (task.isSuccessful) {
                Toast.makeText(this, "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, task.exception?.message ?: "Gửi email thất bại", Toast.LENGTH_LONG).show()
            }
        }
    }
}