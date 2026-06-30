package com.example.shoeshop.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoeshop.R
import com.example.shoeshop.model.Cart
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var itemList: List<Cart>,
    private val onTotalChanged: () -> Unit,
    private val onQuantityChanged: (item: Cart, newQuantity: Int, oldQuantity: Int) -> Unit,
    private val onRemoveItem: (item: Cart) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtProductColor: TextView = itemView.findViewById(R.id.txtProductColor)
        val txtProductSize: TextView = itemView.findViewById(R.id.txtProductSize)
        val txtProductPrice: TextView = itemView.findViewById(R.id.txtProductPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val btnMinus: ImageView = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageView = itemView.findViewById(R.id.btnPlus)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = itemList[position]

        holder.txtProductName.text = item.name
        holder.txtProductColor.text = item.color
        holder.txtProductSize.text = item.size
        holder.txtQuantity.text = item.quantity.toString()

        val format = NumberFormat.getInstance(Locale("vi", "VN"))
        holder.txtProductPrice.text = "${format.format(item.price)}đ"

        holder.cbSelect.setOnCheckedChangeListener(null)
        holder.cbSelect.isChecked = item.isChecked
        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
            onTotalChanged()
        }

        val imagePath = item.image ?: ""
        val imageUrl = if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            imagePath
        } else {
            "http://10.0.2.2:8080/images/$imagePath"
        }

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.banner_placeholder)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.imgProduct)

        holder.btnPlus.setOnClickListener {
            if (item.quantity >= item.stockQuantity) {
                Toast.makeText(holder.itemView.context, "Đã đạt số lượng tối đa trong kho", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val oldQty = item.quantity
            val newQty = oldQty + 1
            item.quantity = newQty
            holder.txtQuantity.text = newQty.toString()
            onTotalChanged()
            onQuantityChanged(item, newQty, oldQty)
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                val oldQty = item.quantity
                val newQty = oldQty - 1
                item.quantity = newQty
                holder.txtQuantity.text = newQty.toString()
                onTotalChanged()
                onQuantityChanged(item, newQty, oldQty)
            }
        }

        holder.btnRemove.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa \"${item.name}\" khỏi giỏ hàng?")
                .setPositiveButton("Xóa") { dialog, _ ->
                    onRemoveItem(item)
                    dialog.dismiss()
                }
                .setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateData(newList: List<Cart>) {
        this.itemList = newList
        notifyDataSetChanged()
    }

    fun refreshItem(cartItemId: Int) {
        val index = itemList.indexOfFirst { it.cartItemId == cartItemId }
        if (index != -1) {
            notifyItemChanged(index)
        }
    }
}