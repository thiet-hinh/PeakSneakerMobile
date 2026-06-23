package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.OrderAdapter
import com.example.shoeshop.enums.OrderStatus
import com.google.android.material.tabs.TabLayout
import dto.respone.OrderReponse

class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var adapter: OrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabOrder = view.findViewById<TabLayout>(R.id.tabOrder)
        val recyclerViewOrder = view.findViewById<RecyclerView>(R.id.recyclerViewOrder)

        adapter = OrderAdapter()

        recyclerViewOrder.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOrder.adapter = adapter

        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.PROCESSING.status))
        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.SHIPPING.status))
        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.DELIVERED.status))

        loadProcessingOrders()

        tabOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                when(tab.position) {
                    0 -> loadProcessingOrders()
                    1 -> loadProcessingOrders()//loadShippingOrders()
                    2 -> loadProcessingOrders()//loadDeliveredOrders()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun loadProcessingOrders() {

        adapter.submitList(
            listOf(
                OrderReponse(
                    image = "https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg",
                    orderCode = "DH001",
                    productName = "Nike Air Force 1",
                    size = "42",
                    color = "White",
                    orderDate = "23/06/2026",
                    price = "2.500.000đ",
                    status = OrderStatus.PROCESSING
                ),
                OrderReponse(
                    image ="https://res.cloudinary.com/duypvtz5w/image/upload/v1780831537/smart-man.jpg",
                    orderCode = "DH002",
                    productName = "Jordan 1 Low",
                    size = "41",
                    color = "Black",
                    orderDate = "22/06/2026",
                    price = "3.200.000đ",
                    status = OrderStatus.PROCESSING
                )
            )
        )
    }
}