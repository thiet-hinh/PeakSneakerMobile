package com.example.shoeshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.shoeshop.R
import com.example.shoeshop.dto.respone.AddressItem
class AddressSpinnerAdapter(context: Context, private val items: List<AddressItem>) :
    ArrayAdapter<AddressItem>(context, 0, items) {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): AddressItem = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        tvName.text = items[position].name
        return view
    }
}