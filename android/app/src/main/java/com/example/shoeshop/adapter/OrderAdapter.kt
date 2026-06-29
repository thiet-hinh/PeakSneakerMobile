package com.example.shoeshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.dto.respone.OrderResponse
import com.example.shoeshop.enums.OrderStatus
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private var orders: List<OrderResponse> = emptyList(),
    private val onDetailClick: (OrderResponse) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val currencyFormatter =
        NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

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

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orders[position]

        holder.txtOrderCode.text = "Mã đơn: DH${order.orderCode.padStart(3, '0')}"
        holder.txtOrderDate.text = "Ngày đặt: ${order.orderDate}"

        holder.txtPrice.text = currencyFormatter.format(order.price)
            .replace(" ₫", "đ")

        when (order.status) {
            OrderStatus.PROCESSING.name -> {
                holder.txtStatus.text = "Đang xử lý"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_processing_status)
            }

            OrderStatus.SHIPPING.name -> {
                holder.txtStatus.text = "Đang giao"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_shipping_status)
            }

            OrderStatus.DELIVERED.name -> {
                holder.txtStatus.text = "Đã giao"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_delivered_status)
            }

            OrderStatus.CANCELLED.name -> {
                holder.txtStatus.text = "Đã hủy"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_order_delivered_status)
            }
        }

        holder.btnDetail.setOnClickListener {
            onDetailClick(order)
        }
    }

    fun submitList(newOrders: List<OrderResponse>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}