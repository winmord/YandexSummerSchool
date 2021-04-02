package com.example.yandexsummerschool.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.File

class FavouriteStockStore(cacheStore: SharedPreferences) {
    private var _favouriteStocks = ArrayList<Stock>()
    private val _fav = MutableLiveData<ArrayList<Stock>>()
    private var _symbols = HashSet<String>()
    private val _cacheStore = cacheStore
    private val _cacheStoreEditor = _cacheStore.edit()

    fun onFavouriteChange(stock: Stock) {
        if (stock in _favouriteStocks) {
            _favouriteStocks.remove(stock)
            removeFromCache(stock)
        } else {
            _favouriteStocks.add(stock)
            addToCache(stock)
        }
    }

    private fun restore() {
        _symbols = _cacheStore.getStringSet("favourites", HashSet<String>()) as HashSet<String>
    }

    private fun addToCache(stock: Stock) {
        val stockName = stock.getStockName()
        _symbols.add(stockName)
        saveCache()
    }

    private fun removeFromCache(stock: Stock) {
        val stockName = stock.getStockName()
        if(stockName in _symbols) {
            _symbols.remove(stockName)
        }
        saveCache()
    }

    private fun saveCache() {
        _cacheStoreEditor.remove("favourites")
        _cacheStoreEditor.apply()
        _cacheStoreEditor.putStringSet("favourites", _symbols)
        _cacheStoreEditor.apply()
    }

    fun getRestoredSymbols(): HashSet<String> {
        restore()
        return _symbols
    }

    fun getAllFavourite(): LiveData<ArrayList<Stock>> {
        _fav.postValue(_favouriteStocks)
        return _fav
    }

    fun clear() {
        _favouriteStocks.clear()
    }
}