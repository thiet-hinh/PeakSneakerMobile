package com.example.shoeshop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoeshop.databinding.ItemOrderProductBinding
import com.example.shoeshop.dto.respone.CheckoutCartItem
import java.text.NumberFormat
import java.util.Locale


class CheckoutItemAdapter(
    private val items: MutableList<CheckoutCartItem> = mutableListOf()
) : RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CheckoutCartItem) {
            binding.tvBrand.text = item.brand.uppercase()
            binding.tvProductName.text = item.productName
            binding.tvColor.text = "Màu: ${item.color}"
            binding.tvSize.text = "Size: ${item.size}"
            binding.tvQuantity.text = "Số lượng: ${item.quantity}"
            binding.tvPrice.text = formatPrice(item.price)

            Glide.with(binding.root.context).load(item.imageUrl).into(binding.imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun submitList(data: List<CheckoutCartItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    private fun formatPrice(price: Double): String {
        return NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price) + "đ"
    }
}