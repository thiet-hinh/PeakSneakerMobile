package com.example.shoeshop.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.shoeshop.R
import com.example.shoeshop.activity.LoginActivity
import com.example.shoeshop.databinding.FragmentProfileBinding
import com.example.shoeshop.utils.PrefManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.imgAvatar.setImageURI(it)
                // TODO: Upload avatar lên server ở đây
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupProfile()
        setupEvents()
    }

    private fun setupProfile() {
        binding.txtName.text = "Peak Collector"
        binding.txtEmail.text = "collector@peaksneaker.com"
        // TODO: Load user từ API ở đây
    }

    private fun setupEvents() {
        // Sự kiện click cho Avatar và nút sửa Avatar
        binding.imgAvatar.setOnClickListener { openGallery() }

        binding.btnEditAvatar.setOnClickListener {
            // TODO: Mở EditProfileActivity (Đã xóa bỏ dòng trùng lặp mở Gallery)
            Toast.makeText(requireContext(), "Mở chỉnh sửa hồ sơ", Toast.LENGTH_SHORT).show()
        }

        // Các mục tính năng khác
        binding.itemAddress.setOnClickListener {
            // TODO: Mở AddressActivity
        }

        binding.itemChangePassword.setOnClickListener {
            // TODO: Mở ChangePasswordActivity
        }

        binding.itemSetting.setOnClickListener {
            // TODO: Mở SettingActivity
        }

        binding.itemSupport.setOnClickListener {
            // TODO: Mở SupportActivity
        }

        // Sự kiện Click cho nút Đăng xuất
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun logout() {
        // Hiển thị Toast thông báo để chắc chắn hàm này được gọi trúng
        Toast.makeText(requireContext(), "Đang đăng xuất...", Toast.LENGTH_SHORT).show()

        // Xóa dữ liệu đăng nhập cũ trong SharePreferences
        PrefManager.clear(requireContext())

        // Chuyển hướng về màn hình Login và xóa hết tất cả các Activity trước đó khỏi Task
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}