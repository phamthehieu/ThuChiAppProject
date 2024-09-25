package com.example.thuchiapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.thuchiapp.R
import com.example.thuchiapp.data.NameMoneyItem

class MoneySpinnerAdapter(
    context: Context,
    private val items: List<NameMoneyItem>
): ArrayAdapter<NameMoneyItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner_money, parent, false)

        val item = getItem(position)
        val nameTextView = view.findViewById<TextView>(R.id.nameMoney)

        item?.let {
            nameTextView.text = context.getString(it.nameMoney)
        }

        return view
    }

}