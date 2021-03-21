package com.example.yandexsummerschool.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import com.example.yandexsummerschool.R


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
//import okhttp3.*
import kotlin.collections.ArrayList

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

class RecyclerViewAdapter(private val onFavoriteChange: (Stock) -> Unit) :
    RecyclerView.Adapter<RecyclerViewAdapter.StockHolder>() {

    private var _stocks: ArrayList<Stock> = arrayListOf()

    fun setStocks(stocks: ArrayList<Stock>) {
        _stocks = stocks
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val inflatedView = parent.inflate(R.layout.stock, false)
        return StockHolder(inflatedView) { pos: Int, fav: Boolean ->
            val stock = _stocks[pos]
            stock.favourite = fav
            onFavoriteChange(stock)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        val stock: Stock = _stocks[position]
        holder.bindStock(stock)
        holder.setOddStocksBackground(position)
    }

    override fun getItemCount(): Int {
        return _stocks.size
    }


    @RequiresApi(Build.VERSION_CODES.M)
    class StockHolder(itemView: View, private val onFavouriteChange: (Int, Boolean) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var stock_name: TextView = itemView.findViewById(R.id.stock_name)
        private var company_name: TextView = itemView.findViewById(R.id.company_name)
        private var current_price: TextView = itemView.findViewById(R.id.current_price)
        private var day_delta: TextView = itemView.findViewById(R.id.day_delta)
        private var buttonFav: Button = itemView.findViewById(R.id.favButton)
        private var buttonFavState = false

        init {
            buttonFav.setOnClickListener(this)
        }

        fun bindStock(stock: Stock) {
            //imageView.setImageResource(stock.getImageResource())
            stock_name.text = stock.getStockName()
            company_name.text = stock.getCompanyName()
            current_price.text = stock.getCurrentPrice()
            day_delta.text = stock.getDayDelta()
            if(!stock.isDayDeltaPositive()) {
                day_delta.setTextColor(Color.parseColor("#B32424"))
            } else {
                day_delta.setTextColor(Color.parseColor("#24B35D"))
            }
            buttonFavState = stock.favourite
            buttonFav.foreground = getDrawable(
                itemView.context,
                if (stock.favourite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            )

            val logoUrl = stock.getLogoUrl()
            if (logoUrl.isNotEmpty()) {
                Picasso.with(itemView.context).load(logoUrl).into(imageView)
            }
        }

        fun setOddStocksBackground(pos: Int) {
            if (pos % 2 == 0) {
                val color = getDrawable(itemView.context, android.R.color.holo_blue_light)
                color?.alpha = 20
                itemView.background = color
            } else {
                val color = getDrawable(itemView.context, android.R.color.white)
                itemView.background = color
            }
        }

        override fun onClick(v: View?) {
            if (buttonFavState) {
                buttonFav.foreground = getDrawable(v!!.context, android.R.drawable.btn_star_big_off)
                buttonFavState = false
            } else {
                buttonFav.foreground = getDrawable(v!!.context, android.R.drawable.btn_star_big_on)
                buttonFavState = true
            }
            onFavouriteChange(adapterPosition, buttonFavState)
        }
    }
}