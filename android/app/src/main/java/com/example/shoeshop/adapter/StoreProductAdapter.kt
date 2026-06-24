package com.example.shoeshop.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoeshop.R
import com.example.shoeshop.activity.ProductDetailActivity
import com.example.shoeshop.model.Product

class StoreProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<StoreProductAdapter.StoreProductViewHolder>() {

    class StoreProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCurrentPrice: TextView = view.findViewById(R.id.tvCurrentPrice)
        val tvOriginalPrice: TextView = view.findViewById(R.id.tvOriginalPrice)
        val tvDiscount: TextView = view.findViewById(R.id.tvDiscount)
        val tvRatingSold: TextView = view.findViewById(R.id.tvRatingSold)
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_store, parent, false)
        return StoreProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvBrand.text = product.brandName
        holder.tvName.text = product.name
        holder.tvCurrentPrice.text = product.price

        holder.tvOriginalPrice.text = product.originalPrice
        holder.tvOriginalPrice.paintFlags = holder.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if (product.discount.isNotEmpty()) {
            holder.tvDiscount.visibility = View.VISIBLE
            holder.tvDiscount.text = product.discount
        } else {
            holder.tvDiscount.visibility = View.GONE
        }

        holder.tvRatingSold.text = "${product.rating}   Đã bán ${product.sold}"
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
        if (product.imageResId != 0) {
            holder.imgProduct.setImageResource(product.imageResId)
        } else {
            holder.imgProduct.setImageResource(R.drawable.banner_placeholder)
        }
    }

    override fun getItemCount() = productList.size
}