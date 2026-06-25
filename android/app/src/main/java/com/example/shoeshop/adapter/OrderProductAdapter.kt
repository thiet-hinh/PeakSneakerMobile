package com.example.shoeshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoeshop.databinding.ItemOrderProductBinding
import com.example.shoeshop.dto.respone.OrderProduct
import java.text.NumberFormat
import java.util.Locale

class OrderProductAdapter(
    private val items: MutableList<OrderProduct> = mutableListOf()
) : RecyclerView.Adapter<OrderProductAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemOrderProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderProduct) {
            binding.tvBrand.text = item.brand.uppercase()
            binding.tvProductName.text = item.productName
            binding.tvSize.text = "Size: ${item.size}"
            binding.tvColor.text = "color: ${item.color}"
            binding.tvQuantity.text = "Số lượng: ${item.quantity}"
            binding.tvPrice.text = formatPrice(item.price)

            Glide.with(binding.root.context).load(item.imageUrl).into(binding.imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemOrderProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    fun submitList(data: List<OrderProduct>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    private fun formatPrice(price: Double) =
        NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price) + "đ"
}