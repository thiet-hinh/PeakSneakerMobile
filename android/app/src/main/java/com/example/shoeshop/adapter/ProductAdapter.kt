package com.example.shoeshop.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoeshop.R
import com.example.shoeshop.activity.ProductDetailActivity
import com.example.shoeshop.model.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val productList: List<Product>,
    private val isGrid: Boolean
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvSold: TextView = view.findViewById(R.id.tvSold)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvOriginalPrice: TextView? = view.findViewById(R.id.tvOriginalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layout = if (isGrid) R.layout.item_product_grid else R.layout.item_product_featured
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvBrand.text = product.brandName
        holder.tvName.text = product.name
        holder.tvRating.text = product.rating
        holder.tvSold.text = "| Đã bán ${product.sold}"

        holder.tvPrice.text = formatCurrency(product.price)

        holder.tvOriginalPrice?.let { tvOriginal ->
            if (product.discountRate > 0) {
                tvOriginal.visibility = View.VISIBLE
                tvOriginal.text = formatCurrency(product.basePrice)
                tvOriginal.paintFlags = tvOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Nếu không giảm giá thì ẩn giá gốc đi
                tvOriginal.visibility = View.GONE
            }
        }

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.banner_placeholder)
            .error(R.drawable.banner_placeholder)
            .into(holder.imgProduct)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = productList.size


    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return "${formatter.format(amount)}đ"
    }
}