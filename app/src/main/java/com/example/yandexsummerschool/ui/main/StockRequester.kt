package com.example.yandexsummerschool.ui.main

import android.graphics.Bitmap
import android.net.Uri.Builder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class StockRequester(favouriteStockStore: FavouriteStockStore, cacheDir: String) {
    private val _client: OkHttpClient = OkHttpClient()
    private val _favouriteStockStore = favouriteStockStore

    private var _encryptedT = "rma2h;::h62:2dh6ddec59cc968e8d6e78e"
    private val _cacheDir = cacheDir

    private val _mostActiveStocks = MutableLiveData<ArrayList<Stock>>()
    private val _mostActiveStocksLogos = MutableLiveData<Int>()
    private var _stocks = ArrayList<Stock>()
    private var _restoredFavouriteStocks = HashSet<String>()

    init {
        _encryptedT = decrypt()
        _restoredFavouriteStocks = _favouriteStockStore.getRestoredSymbols()
        getStocks()
    }

    fun update() {
        try {
            _stocks.clear()
            _favouriteStockStore.clear()
            _restoredFavouriteStocks = _favouriteStockStore.getRestoredSymbols()
            getStocks()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getStocks() {
        val urlRequest = Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_STABLE)
            .appendPath(URL_PATH_STOCK)
            .appendPath(URL_PATH_MARKET)
            .appendPath(URL_PATH_COLLECTION)
            .appendPath(URL_PATH_LIST)
            .appendQueryParameter(URL_QUERY_PARAM_COLLECTION_NAME, "mostactive")
            .appendQueryParameter(URL_QUERY_PARAM_TOKEN, _encryptedT)
            .appendQueryParameter(URL_QUERY_LIMIT_COUNT, "30")
            .build().toString()

        val request = Request.Builder().url(urlRequest).build()

        _client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseStocks = ArrayList<String>()
                    val stockJsonArray = JSONArray(response.body()!!.string())
                    for (stockNumber in 0 until stockJsonArray.length()) {
                        try {
                            val stockJson = JSONObject(stockJsonArray[stockNumber].toString())
                            val stock = Stock(stockJson)
                            getLogo(stock)

                            val stockName = stock.getStockName()
                            if (stockName in _restoredFavouriteStocks) {
                                stock.setFavourite(true)
                                _favouriteStockStore.onFavouriteChange(stock)
                            }

                            responseStocks.add(stockName)
                            _stocks.add(stock)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    for (restoredStock in _restoredFavouriteStocks) {
                        if (restoredStock !in responseStocks) {
                            try {
                                askForNonMostActiveStock(restoredStock)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    _mostActiveStocks.postValue(_stocks)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun askForNonMostActiveStock(stockName: String) {
        val urlRequest = Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_STABLE)
            .appendPath(URL_PATH_STOCK)
            .appendPath(stockName)
            .appendPath(URL_PATH_BATCH)
            .appendQueryParameter(URL_QUERY_TYPES, "quote")
            .appendQueryParameter(URL_QUERY_PARAM_TOKEN, _encryptedT)
            .build().toString()

        val request = Request.Builder().url(urlRequest).build()

        _client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val stockJson = JSONObject(response.body()!!.string())
                    val stock = Stock(JSONObject(stockJson["quote"].toString()))
                    getLogo(stock)
                    stock.setFavourite(true)
                    _favouriteStockStore.onFavouriteChange(stock)
                    _stocks.add(stock)
                    _mostActiveStocks.postValue(_stocks)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun searchStocks(query: String) {
        val urlRequest = Builder().scheme(URL_SCHEME)
            .authority(URL_SANDBOX)
            .appendPath(URL_PATH_STABLE)
            .appendPath(URL_PATH_SEARCH)
            .appendPath(query)
            .appendQueryParameter(URL_QUERY_PARAM_TOKEN, "Tpk_bfb2e6499e434d26a35c1e092bb606da")
            .build().toString()

        val request = Request.Builder().url(urlRequest).build()

        _client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val stockJson = JSONArray(response.body()!!.string())
                    Log.d("SEARCH", stockJson.toString())
                    /*val stock = Stock(JSONObject(stockJson["quote"].toString()))
                    getLogo(stock)
                    stock.setFavourite(true)
                    _favouriteStockStore.onFavouriteChange(stock)
                    _stocks.add(stock)
                    _mostActiveStocks.postValue(_stocks)*/
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getLogo(stock: Stock) {
        val stockName = stock.getStockName()
        val logoFile = File("$_cacheDir/$stockName.jpeg")
        val bitmap: Bitmap
        if (logoFile.exists()) {
            bitmap = Picasso.get().load(logoFile).get()
            stock.setLogoBitmap(bitmap)
        } else {
            stock.setLogoBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
            val urlRequest = Builder().scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_PATH_STABLE)
                .appendPath(URL_PATH_STOCK)
                .appendPath(stock.getStockName().toLowerCase(Locale.ROOT))
                .appendPath(URL_PATH_LOGO)
                .appendQueryParameter(URL_QUERY_PARAM_TOKEN, _encryptedT)
                .build().toString()

            val request = Request.Builder().url(urlRequest).build()
            _client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val logoJSON = JSONObject(response.body()!!.string())
                        val logoUrl = logoJSON.getString("url")

                        val loadedBitmap = Picasso.get().load(logoUrl).get()
                        loadedBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            FileOutputStream(logoFile)
                        )

                        stock.setLogoBitmap(loadedBitmap)
                        _mostActiveStocksLogos.postValue(0)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun getMostActiveStocks(): LiveData<ArrayList<Stock>> {
        return _mostActiveStocks
    }

    fun getLogos(): LiveData<Int> {
        return _mostActiveStocksLogos
    }

    private fun decrypt(): String {
        var decryptedT = ""
        for (symbol in _encryptedT) {
            decryptedT += symbol - 2
        }

        return decryptedT
    }

    companion object {
        private const val URL_SCHEME = "https"
        private const val URL_AUTHORITY = "cloud.iexapis.com"
        private const val URL_SANDBOX = "sandbox.iexapis.com"
        private const val URL_PATH_STABLE = "stable"
        private const val URL_PATH_SEARCH = "search"
        private const val URL_PATH_STOCK = "stock"
        private const val URL_PATH_MARKET = "market"
        private const val URL_PATH_COLLECTION = "collection"
        private const val URL_PATH_LIST = "list"
        private const val URL_PATH_LOGO = "logo"
        private const val URL_PATH_BATCH = "batch"
        private const val URL_QUERY_TYPES = "types"
        private const val URL_QUERY_PARAM_COLLECTION_NAME = "collectionName"
        private const val URL_QUERY_PARAM_TOKEN = "token"
        private const val URL_QUERY_LIMIT_COUNT = "listLimit"
    }
}