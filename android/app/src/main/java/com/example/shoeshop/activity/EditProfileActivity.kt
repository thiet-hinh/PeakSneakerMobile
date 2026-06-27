package com.example.shoeshop.activity

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.databinding.EditProfileActivityBinding
import com.example.shoeshop.dto.request.UpdateProfileRequest
import com.example.shoeshop.retrofit.UserRetrofit
import com.example.shoeshop.utils.PrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: EditProfileActivityBinding
    private var userId: Int = -1

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult
        binding.imgAvatar.setImageURI(uri)
        uploadAvatar(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = PrefManager.getUser(this)?.id ?: -1
        binding.header.tvTitle.text = "Chỉnh Sửa Thông Tin"
        binding.header.btnBack.setOnClickListener { finish() }
        binding.imgAvatar.setOnClickListener { openGallery() }
        binding.btnSave.setOnClickListener { updateProfile() }
        loadProfile()
        loadAvatar()
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val response = UserRetrofit.api.getProfile(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        binding.edtFirstName.setText(it.firstName)
                        binding.edtLastName.setText(it.lastName)
                        binding.edtEmail.setText(it.email)
                        binding.edtPhone.setText(it.phone)
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "Không tải được thông tin", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAvatar() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = UserRetrofit.api.getAvatar(userId)
                Log.d("AVATAR", "HTTP=${response.code()}")
                if (!response.isSuccessful) {
                    Log.e("AVATAR", response.errorBody()?.string() ?: "")
                    return@launch
                }
                val bytes = response.body()?.bytes()
                if (bytes == null) {
                    return@launch
                }
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (bitmap != null) {
                    withContext(Dispatchers.Main) {
                        binding.imgAvatar.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                Log.e("AVATAR", "Exception", e)
            }
        }
    }

    private fun updateProfile() {
        val request = UpdateProfileRequest(
            firstName = binding.edtFirstName.text.toString().trim(),
            lastName = binding.edtLastName.text.toString().trim(),
            phone = binding.edtPhone.text.toString().trim()
        )
        lifecycleScope.launch {
            try {
                val response = UserRetrofit.api.updateProfile(userId, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Không thể cập nhật", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadAvatar(uri: Uri) {
        lifecycleScope.launch {
            try {
                val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: return@launch
                val requestBody = bytes.toRequestBody(
                    "application/octet-stream".toMediaType()
                )
                val response = UserRetrofit.api.updateAvatar(userId, requestBody)

                if (!response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Không thể cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() = pickImageLauncher.launch("image/*")
}