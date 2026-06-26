package com.example.shoeshop.activity


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.R
import com.example.shoeshop.databinding.RegisterActivityBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.dto.request.RegisterRequest
import com.example.shoeshop.retrofit.UserRetrofit
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupPasswordToggle()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener { registerAccount() }

        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Đăng ký Google đang phát triển", Toast.LENGTH_SHORT).show()
        }

        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun setupPasswordToggle() {
        binding.btnTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnTogglePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            }

            binding.edtPassword.setSelection(binding.edtPassword.text.length)
        }

        binding.btnToggleConfirmPassword.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible

            if (confirmPasswordVisible) {
                binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility)
            } else {
                binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off)
            }

            binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
        }
    }

    private fun registerAccount() {
        val lastName = binding.edtLastName.text.toString().trim()
        val firstName = binding.edtFirstName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        when {
            lastName.isEmpty() -> {
                binding.edtLastName.error = "Nhập họ"
                return
            }

            firstName.isEmpty() -> {
                binding.edtFirstName.error = "Nhập tên"
                return
            }

            email.isEmpty() -> {
                binding.edtEmail.error = "Nhập email"
                return
            }

            phone.isEmpty() -> {
                binding.edtPhone.error = "Nhập số điện thoại"
                return
            }

            password.length < 6 -> {
                binding.edtPassword.error = "Mật khẩu tối thiểu 6 ký tự"
                return
            }

            password != confirmPassword -> {
                binding.edtConfirmPassword.error = "Mật khẩu không khớp"
                return
            }

            !binding.cbTerms.isChecked -> {
                Toast.makeText(this, "Vui lòng đồng ý điều khoản", Toast.LENGTH_SHORT).show()
                return
            }
        }

        binding.btnRegister.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                binding.btnRegister.isEnabled = true
                val exception = task.exception
                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        Log.e("Register", "Email đã tồn tại", exception)
                        Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Log.e("Register", "Firebase đăng ký thất bại", exception)
                        Toast.makeText(this, exception?.localizedMessage ?: "Đăng ký thất bại", Toast.LENGTH_LONG).show()
                    }
                }
                return@addOnCompleteListener
            }

            val firebaseUser = task.result.user!!
            val firebaseUid = firebaseUser.uid
            Log.d("Register", "Firebase đăng ký thành công. UID = $firebaseUid")

            val request = RegisterRequest(
                firebaseUid = firebaseUid,
                firstname = firstName,
                lastname = lastName,
                email = email,
                phone = phone
            )

            lifecycleScope.launch {
                try {
                    val response = UserRetrofit.api.register(request)
                    binding.btnRegister.isEnabled = true

                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("Register", "Spring Boot trả về lỗi code = ${response.code()}")
                        Log.e("Register", "Body = ${response.errorBody()?.string()}")
                        firebaseUser.delete()
                        Toast.makeText(this@RegisterActivity, "Không thể lưu người dùng lên hệ thống.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    binding.btnRegister.isEnabled = true
                    Log.e("Register", "Không gọi được API Spring Boot", e)
                    firebaseUser.delete()
                    Toast.makeText(this@RegisterActivity, e.localizedMessage ?: "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}