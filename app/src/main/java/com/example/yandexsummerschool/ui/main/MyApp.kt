package com.example.yandexsummerschool.ui.main

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.yandexsummerschool.R

class MyApp: Application() {

    private lateinit var _favouriteStockStore: FavouriteStockStore
    private lateinit var _stockRequester: StockRequester
    private lateinit var _cacheStore: SharedPreferences

    val favouriteStockStore: FavouriteStockStore
        get() = _favouriteStockStore

    val stockRequester: StockRequester
        get() = _stockRequester

    override fun onCreate() {
        super.onCreate()

        val cacheDir = this.cacheDir.toString()
        _cacheStore = this.getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        _favouriteStockStore = FavouriteStockStore(_cacheStore)
        _stockRequester = StockRequester(_favouriteStockStore, cacheDir)
    }

    companion object {
        val Context.app: MyApp
            get() = applicationContext as MyApp
    }
}
