package com.example.shoeshop.adapter // Đổi theo package của bạn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.model.Cart
import java.text.NumberFormat
import java.util.Locale
class CartAdapter(
    private val itemList: List<Cart>,
    private val onTotalChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbSelect: CheckBox = view.findViewById(R.id.cbSelect)
        val txtName: TextView = view.findViewById(R.id.txtProductName)
        val txtSize: TextView = view.findViewById(R.id.txtProductSize)
        val txtPrice: TextView = view.findViewById(R.id.txtProductPrice)
        val txtQuantity: TextView = view.findViewById(R.id.txtQuantity)

        val btnPlus: ImageView = view.findViewById(R.id.btnPlus)
        val btnMinus: ImageView = view.findViewById(R.id.btnMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = itemList[position]

        holder.txtName.text = item.name
        holder.txtSize.text = item.size
        holder.txtQuantity.text = item.quantity.toString()

        val format = NumberFormat.getInstance(Locale("vi", "VN"))
        holder.txtPrice.text = "${format.format(item.price)}đ"

        holder.cbSelect.setOnCheckedChangeListener(null)
        holder.cbSelect.isChecked = item.isChecked

        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
            onTotalChanged()
        }

        holder.btnPlus.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onTotalChanged()
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
                onTotalChanged()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size
}