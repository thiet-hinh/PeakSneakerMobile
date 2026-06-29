package com.example.shoeshop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoeshop.R

class PageFragment : Fragment() {
    companion object {
        fun newInstance(title: String): PageFragment {
            val fragment = PageFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        val txtPage = view.findViewById<TextView>(R.id.txtPage)
        txtPage.text = arguments?.getString("title")
        return view
    }
}