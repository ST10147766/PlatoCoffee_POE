package com.example.platocoffee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class CoffeeAdapter(private val context: Context, private val coffeeItems: List<CoffeeItem>) :
    BaseAdapter() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return coffeeItems.size
    }

    override fun getItem(position: Int): Any {
        return coffeeItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.coffee_item, parent, false)
        }
        val item = coffeeItems[position]
        val coffeeImage = convertView.findViewById<ImageView>(R.id.coffeeImage)
        val ratingText = convertView.findViewById<TextView>(R.id.ratingText)
        val coffeeName = convertView.findViewById<TextView>(R.id.coffeeName)
        val coffeePrice = convertView.findViewById<TextView>(R.id.coffeePrice)
        val addButton = convertView.findViewById<Button>(R.id.addButton)

        // Set coffee image
        coffeeImage.setImageResource(item.imageResource)
        ratingText.text = item.rating.toString()
        coffeeName.text = item.name
        coffeePrice.text = "R" + String.format("%.2f", item.price)
        return convertView
    }
}