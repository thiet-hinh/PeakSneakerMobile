package com.example.shoeshop.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.activity.OrderDetailActivity
import com.example.shoeshop.adapter.OrderAdapter
import com.example.shoeshop.enums.OrderStatus
import com.example.shoeshop.retrofit.OrderRetrofit
import com.example.shoeshop.utils.PrefManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class OrderFragment : Fragment(R.layout.fragment_order) {
    private lateinit var adapter: OrderAdapter
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var tabOrder: TabLayout
    private var userId = -1

    private val orderDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) reloadCurrentTab()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = PrefManager.getUser(requireContext())?.id ?: -1
        tabOrder = view.findViewById(R.id.tabOrder)
        val recyclerViewOrder = view.findViewById<RecyclerView>(R.id.recyclerViewOrder)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)
        adapter = OrderAdapter { order ->
            val intent = Intent(requireContext(), OrderDetailActivity::class.java).apply { putExtra("ORDER_ID", order.orderCode) }
            orderDetailLauncher.launch(intent)
        }
        recyclerViewOrder.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOrder.adapter = adapter
        tabOrder.addTab(tabOrder.newTab().setText("Chờ xử lý"))
        tabOrder.addTab(tabOrder.newTab().setText("Đang giao"))
        tabOrder.addTab(tabOrder.newTab().setText("Đã giao"))
        tabOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { reloadCurrentTab() }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        reloadCurrentTab()
    }

    private fun reloadCurrentTab() {
        if (userId == -1) {
            Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show()
            return
        }
        when (tabOrder.selectedTabPosition) {
            0 -> showOrders(userId, OrderStatus.PROCESSING)
            1 -> showOrders(userId, OrderStatus.SHIPPING)
            2 -> showOrders(userId, OrderStatus.DELIVERED)
        }
    }

    private fun showOrders(userId: Int, status: OrderStatus) {
        if (userId == -1) {
            Toast.makeText(requireContext(), "Không tìm thấy user", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                val orderList = OrderRetrofit.api.getOrdersByUserAndStatus(userId, status.name)
                adapter.submitList(orderList)
                layoutEmpty.visibility = if (orderList.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi tải dữ liệu hoặc lỗi kết nối: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                adapter.submitList(emptyList())
                layoutEmpty.visibility = View.VISIBLE
            }
        }
    }
}