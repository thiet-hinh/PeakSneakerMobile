package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.R
import com.example.shoeshop.databinding.LoginActivityBinding
import com.example.shoeshop.dto.request.RegisterRequest
import com.example.shoeshop.model.User
import com.example.shoeshop.retrofit.UserRetrofit
import com.example.shoeshop.utils.PrefManager
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val googleIdOption = GetSignInWithGoogleOption.Builder(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleGoogleCredential(result.credential)
            } catch (e: GetCredentialException) {
                Log.e("GoogleLogin", "Credential Manager error", e)
                Toast.makeText(this@LoginActivity, "Đăng nhập Google thất bại", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleGoogleCredential(credential: Credential) {
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            Toast.makeText(this, "Loại thông tin xác thực không hợp lệ", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            val firstName = googleIdTokenCredential.givenName ?: "User"
            val lastName = googleIdTokenCredential.familyName ?: ""
            val email = googleIdTokenCredential.id

            firebaseAuthWithGoogle(idToken, firstName, lastName, email)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("GoogleLogin", "Token parsing failed", e)
            Toast.makeText(this, "Lỗi xử lý thông tin Google", Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, firstName: String, lastName: String, email: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        binding.btnGoogleLogin.isEnabled = false

        auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
            binding.btnGoogleLogin.isEnabled = true

            if (!task.isSuccessful) {
                Log.e("GoogleLogin", "Firebase sign-in failed", task.exception)
                Toast.makeText(this, task.exception?.localizedMessage ?: "Đăng nhập thất bại", Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            val firebaseUid = task.result.user!!.uid
            fetchOrCreateUser(firebaseUid, firstName, lastName, email)
        }
    }

    private fun fetchOrCreateUser(firebaseUid: String, firstName: String, lastName: String, email: String) {
        lifecycleScope.launch {
            try {
                val response = UserRetrofit.api.getUserByFireBaseUid(firebaseUid)

                if (response.isSuccessful && response.body() != null) {
                    onLoginSuccess(response.body()!!)
                } else if (response.code() == 404) {
                    registerGoogleUser(firebaseUid, firstName, lastName, email)
                } else {
                    Log.e("GoogleLogin", "Server error ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Không lấy được thông tin người dùng.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, e.localizedMessage ?: "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun registerGoogleUser(firebaseUid: String, firstName: String, lastName: String, email: String) {
        try {
            val request = RegisterRequest(
                firebaseUid = firebaseUid,
                firstname = firstName,
                lastname = lastName,
                email = email,
                phone = ""
            )
            val response: Response<User> = UserRetrofit.api.register(request)

            if (response.isSuccessful && response.body() != null) {
                onLoginSuccess(response.body()!!)
            } else {
                Log.e("GoogleLogin", "Register error ${response.code()} ${response.errorBody()?.string()}")
                Toast.makeText(this@LoginActivity, "Đăng ký tài khoản thất bại.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@LoginActivity, e.localizedMessage ?: "Có lỗi xảy ra khi đăng ký", Toast.LENGTH_LONG).show()
        }
    }

    private fun onLoginSuccess(user: User) {
        if (isFinishing || isDestroyed) return
        PrefManager.saveUser(applicationContext, user)
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
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
                        onLoginSuccess(user)
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