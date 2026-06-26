package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.databinding.LoginActivityBinding
import com.example.shoeshop.retrofit.UserRetrofit
import com.example.shoeshop.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            login()
        }

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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Đăng nhập"
                Log.e("Login", "Firebase login failed", task.exception)
                Toast.makeText(this, task.exception?.localizedMessage ?: "Đăng nhập thất bại", Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            val firebaseUid = task.result.user!!.uid

            lifecycleScope.launch {
                try {
                    val response = UserRetrofit.api.getUserByFireBaseUid(firebaseUid)
                    if (isFinishing || isDestroyed) return@launch

                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Đăng nhập"

                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user == null) {
                            Toast.makeText(this@LoginActivity, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_LONG).show()
                            return@launch
                        }
                        PrefManager.saveUser(applicationContext, user)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finishAffinity()
                    } else {
                        Log.e("Login", "Server error ${response.code()}")
                        Log.e("Login", response.errorBody()?.string() ?: "")
                        Toast.makeText(this@LoginActivity, "Không lấy được thông tin người dùng.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    if (isFinishing || isDestroyed) return@launch
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Đăng nhập"
                    Toast.makeText(this@LoginActivity, e.localizedMessage ?: "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}