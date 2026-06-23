package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.databinding.LoginActivityBinding
import com.example.shoeshop.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener { }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                binding.edtEmail.error = "Nhập email"
                return
            }

            password.isEmpty() -> {
                binding.edtPassword.error = "Nhập mật khẩu"
                return
            }
        }

        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Đang đăng nhập..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: ""

                PrefManager.saveUid(this, uid)

                startActivity(Intent(this, MainActivity::class.java))

                finishAffinity()
            }
            .addOnFailureListener {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Đăng nhập"

                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}