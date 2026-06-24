package com.example.shoeshop.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.shoeshop.R
import com.example.shoeshop.dto.respone.AddressItem
class AddressSpinnerAdapter(
    context: Context,
    items: List<AddressItem>
) : ArrayAdapter<AddressItem>(context, R.layout.item_spinner, items) {

    init {
        setDropDownViewResource(R.layout.item_spinner)
    }
}