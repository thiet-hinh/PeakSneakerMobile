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

        // 1. Hiển thị Tên thương hiệu và Tên sản phẩm từ Model thực tế
        holder.tvBrand.text = product.brandName
        holder.tvName.text = product.name

        // 2. Định dạng giá bán hiện tại sang VND (Ví dụ: 2.450.000đ)
        holder.tvCurrentPrice.text = formatCurrency(product.price)

        // 3. Xử lý logic Giảm giá & Giá gốc gạch ngang trực tiếp từ discountRate của DB
        if (product.discountRate > 0) {
            // Hiển thị giá gốc kèm gạch ngang
            holder.tvOriginalPrice.visibility = View.VISIBLE
            holder.tvOriginalPrice.text = formatCurrency(product.basePrice)
            holder.tvOriginalPrice.paintFlags = holder.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            // Tính toán và hiển thị % giảm giá (Ví dụ: 0.2 -> -20%)
            holder.tvDiscount.visibility = View.VISIBLE
            val discountPercent = (product.discountRate * 100).toInt()
            holder.tvDiscount.text = "-${discountPercent}%"
        } else {
            // Nếu không giảm giá, ẩn cả giá gốc lẫn tag giảm giá
            holder.tvOriginalPrice.visibility = View.GONE
            holder.tvDiscount.visibility = View.GONE
        }

        // 4. Hiển thị Đánh giá và Số lượng bán
        holder.tvRatingSold.text = "${product.rating}   Đã bán ${product.sold}"

        // 5. Cấu hình Glide tải ảnh động từ link Cloudinary URL
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.banner_placeholder)
            .error(R.drawable.banner_placeholder)
            .into(holder.imgProduct)

        // 6. Truyền ID đôi giày khi nhấn vào Item để ProductDetailActivity truy vấn đúng thông tin
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = productList.size

    /**
     * Hàm định dạng số Double sang dạng tiền tệ Việt Nam (VND)
     */
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return "${formatter.format(amount)}đ"
    }
}