package com.example.yandexsummerschool.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.File

class FavouriteStockStore(cacheDir: String) {
    private var _favouriteStocks = ArrayList<Stock>()
    private val _fav = MutableLiveData<ArrayList<Stock>>()
    private val _symbols = HashSet<String>()
    private val _cacheFile = File("$cacheDir/favourites.cache")

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
        if (_cacheFile.exists()) {
            val cacheArray = JSONArray(_cacheFile.readText())
            for (f in 0 until cacheArray.length()) {
                _symbols.add(cacheArray[f].toString())
            }
        }
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
        val cacheArray = JSONArray(_symbols)
        _cacheFile.writeText(cacheArray.toString())
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