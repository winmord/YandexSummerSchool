package com.example.yandexsummerschool.ui.main

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.yandexsummerschool.R

class MyApp: Application() {

    private lateinit var _favouriteStockStore: FavouriteStockStore
    private lateinit var _stockRequester: StockRequester

    val favouriteStockStore: FavouriteStockStore
        get() = _favouriteStockStore

    val stockRequester: StockRequester
        get() = _stockRequester

    override fun onCreate() {
        super.onCreate()

        val cacheDir = this.cacheDir.toString()
        _favouriteStockStore = FavouriteStockStore(cacheDir)
        _stockRequester = StockRequester(_favouriteStockStore, cacheDir)
    }

    companion object {
        val Context.app: MyApp
            get() = applicationContext as MyApp
    }
}
