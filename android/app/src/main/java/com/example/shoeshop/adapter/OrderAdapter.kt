package com.example.shoeshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.dto.respone.OrderReponse
import com.example.shoeshop.enums.OrderStatus

class OrderAdapter(
    private var orders: List<OrderReponse> = emptyList()
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtOrderCode: TextView = itemView.findViewById(R.id.txtOrderCode)
        val txtOrderDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)

        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)

        val btnDetail: Button = itemView.findViewById(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item_card, parent, false)

        return OrderViewHolder(view)
    }

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orders[position]

        holder.txtOrderCode.text = "Mã đơn: ${order.orderCode}"
        holder.txtOrderDate.text = "Ngày đặt: ${order.orderDate}"
        holder.txtPrice.text = order.price

        when (order.status) {
            OrderStatus.PROCESSING -> {
                holder.txtStatus.text = "Đang xử lí"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_processing_status)
            }

            OrderStatus.SHIPPING -> {
                holder.txtStatus.text = "Đang giao"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_shipping_status)
            }

            OrderStatus.DELIVERED -> {
                holder.txtStatus.text = "Đã giao"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_delivered_status)
            }
        }

        holder.btnDetail.setOnClickListener {

            Toast.makeText(
                holder.itemView.context,
                "Chi tiết đơn ${order.orderCode}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun submitList(newOrders: List<OrderReponse>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}