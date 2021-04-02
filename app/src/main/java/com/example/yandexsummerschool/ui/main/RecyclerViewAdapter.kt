package com.example.yandexsummerschool.ui.main

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
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
import com.example.yandexsummerschool.R

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

class RecyclerViewAdapter(private val onFavoriteChange: (Stock) -> Unit) :
    RecyclerView.Adapter<RecyclerViewAdapter.StockHolder>() {

    private var _stocks: ArrayList<Stock> = arrayListOf()
    private var _logosChanged: Int = 0

    fun setStocks(stocks: List<Stock>) {
        _stocks = stocks as ArrayList<Stock>
        notifyDataSetChanged()
    }

    fun setLogos(logos: Int) {
        _logosChanged = logos
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val inflatedView = parent.inflate(R.layout.stock, false)
        return StockHolder(inflatedView) { pos: Int, fav: Boolean ->
            val stock = _stocks[pos]
            stock.setFavourite(fav)
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
        private var stockName: TextView = itemView.findViewById(R.id.stock_name)
        private var companyName: TextView = itemView.findViewById(R.id.company_name)
        private var currentPrice: TextView = itemView.findViewById(R.id.current_price)
        private var dayDelta: TextView = itemView.findViewById(R.id.day_delta)
        private var buttonFav: Button = itemView.findViewById(R.id.favButton)
        private var buttonFavState = false

        init {
            buttonFav.setOnClickListener(this)
        }

        fun bindStock(stock: Stock) {
            stockName.text = stock.getStockName()
            companyName.text = stock.getCompanyName()
            currentPrice.text = stock.getCurrentPrice()
            dayDelta.text = stock.getDayDelta()
            if (!stock.isDayDeltaPositive()) {
                dayDelta.setTextColor(Color.parseColor("#B32424"))
            } else {
                dayDelta.setTextColor(Color.parseColor("#24B35D"))
            }
            buttonFavState = stock.isFavourite()
            buttonFav.foreground = getDrawable(
                itemView.context,
                if (stock.isFavourite()) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            )

            imageView.setImageBitmap(stock.getLogoBitmap())
        }

        fun setOddStocksBackground(pos: Int) {
            if (pos % 2 == 0) {
                val color = ColorDrawable(Color.parseColor("#F0F4F7"))
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