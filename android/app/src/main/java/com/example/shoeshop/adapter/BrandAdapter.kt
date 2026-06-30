package com.example.shoeshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoeshop.R
import com.example.shoeshop.model.Brand

class BrandAdapter(
    private val brandList: List<Brand>,
    private val onBrandClick: ((Brand) -> Unit)? = null
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgBrand: ImageView = view.findViewById(R.id.imgBrand)
        val tvBrandName: TextView = view.findViewById(R.id.tvBrandName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = brandList[position]
        holder.tvBrandName.text = brand.name

        Glide.with(holder.itemView.context)
            .load(brand.imageUrl)
            .placeholder(R.drawable.banner_placeholder)
            .error(R.drawable.banner_placeholder)
            .into(holder.imgBrand)

        holder.itemView.setOnClickListener {
            onBrandClick?.invoke(brand)
        }
    }

    override fun getItemCount() = brandList.size
}