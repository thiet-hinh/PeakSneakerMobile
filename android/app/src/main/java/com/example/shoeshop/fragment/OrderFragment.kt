package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.adapter.OrderAdapter
import com.example.shoeshop.enums.OrderStatus
import com.google.android.material.tabs.TabLayout
import com.example.shoeshop.dto.respone.OrderReponse

class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var adapter: OrderAdapter

    private lateinit var layoutEmpty: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabOrder = view.findViewById<TabLayout>(R.id.tabOrder)
        val recyclerViewOrder = view.findViewById<RecyclerView>(R.id.recyclerViewOrder)

        adapter = OrderAdapter()
        layoutEmpty = view.findViewById(R.id.layoutEmpty)

        recyclerViewOrder.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOrder.adapter = adapter

        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.PROCESSING.status))
        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.SHIPPING.status))
        tabOrder.addTab(tabOrder.newTab().setText(OrderStatus.DELIVERED.status))

        tabOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                when(tab.position) {
                    0 -> showOrders(null)
                    1 -> showOrders(loadOrders())//loadShippingOrders()
                    2 -> showOrders(loadOrders())//loadDeliveredOrders()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        showOrders(null)
    }

    private fun showOrders(orders: List<OrderReponse>?) {

        if (orders.isNullOrEmpty()) {

            layoutEmpty.visibility = View.VISIBLE

        } else {
            layoutEmpty.visibility = View.GONE
            adapter.submitList(orders)
        }
    }

    private fun loadOrders() : List<OrderReponse> {

       return listOf(
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

    }
}