package com.example.shoeshop.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.shoeshop.R
import com.example.shoeshop.activity.ChangePasswordActivity
import com.example.shoeshop.activity.EditProfileActivity
import com.example.shoeshop.activity.LoginActivity
import com.example.shoeshop.activity.ShippingAddressActivity
import com.example.shoeshop.databinding.FragmentProfileBinding
import com.example.shoeshop.retrofit.UserRetrofit
import com.example.shoeshop.utils.PrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        userId = PrefManager.getUser(requireContext())?.id ?: -1
        loadProfile()
        loadAvatar()
        setupEvents()
    }

    private fun setupEvents() {
        binding.profileEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        binding.itemAddress.setOnClickListener {
            startActivity(Intent(requireContext(), ShippingAddressActivity::class.java))
        }
        binding.itemChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        Toast.makeText(requireContext(), "Đang đăng xuất...", Toast.LENGTH_SHORT).show()
        PrefManager.clearUser(requireContext())
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadAvatar() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = UserRetrofit.api.getAvatar(userId)
                if (!response.isSuccessful) return@launch
                val bytes = response.body()?.bytes() ?: return@launch
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@launch
                withContext(Dispatchers.Main) { binding.imgAvatar.setImageBitmap(bitmap) }
            } catch (e: Exception) {
                Log.e("AVATAR", "Exception", e)
            }
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val response = UserRetrofit.api.getProfile(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        binding.txtName.text = "${it.firstName} ${it.lastName}"
                        binding.txtEmail.text = it.email
                    }
                } else {
                    Toast.makeText(requireContext(), "Không tải được thông tin", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}