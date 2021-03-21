package com.example.yandexsummerschool.ui.main

import android.util.Log

class FavouriteStockStore {
    private var favouriteStocks = ArrayList<Stock>()

    fun onFavouriteChange(stock: Stock) {
        Log.d("AAAA", "AAAA")
        if(stock in favouriteStocks) {
            favouriteStocks.remove(stock)
        } else {
            favouriteStocks.add(stock)
        }
    }

    fun getFavouriteStocks(): ArrayList<Stock> {
        return favouriteStocks
    }
}