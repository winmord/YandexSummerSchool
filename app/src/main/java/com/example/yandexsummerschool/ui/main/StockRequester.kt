package com.example.yandexsummerschool.ui.main

import android.net.Uri.Builder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class StockRequester() {
    private val client: OkHttpClient = OkHttpClient()
    private val _liveStocks = MutableLiveData<ArrayList<Stock>>()
    private var _stocks = ArrayList<Stock>()

    init {
        getStocks()
    }
    private fun getStocks() {
        val urlRequest = Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_PATH_2)
            .appendPath(URL_PATH_3)
            .appendPath(URL_PATH_4)
            .appendPath(URL_PATH_5)
            .appendQueryParameter(URL_QUERY_PARAM_COLLECTION_NAME, "mostactive")
            .appendQueryParameter(URL_QUERY_PARAM_TOKEN, "pk_0f988f4080bf4bbca37aa746c6b4c56c")
            .appendQueryParameter(URL_QUERY_LIMIT_COUNT, "30")
            .build().toString()

        val request = Request.Builder().url(urlRequest).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val stockJsonArray = JSONArray(response.body()!!.string())
                    for (stockNumber in 0 until stockJsonArray.length()) {
                        val stockJson = JSONObject(stockJsonArray[stockNumber].toString())
                        val stock = Stock(stockJson)
                        getLogo(stock)
                        _stocks.add(stock)
                    }
                    _liveStocks.postValue(_stocks)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getLogo(stock : Stock) : String {
        val client: OkHttpClient = OkHttpClient()
        val url = "https://cloud.iexapis.com/stable/stock/${stock.getStockName().toLowerCase(Locale.ROOT)}/logo?token=pk_0f988f4080bf4bbca37aa746c6b4c56c"
        val request = Request.Builder().url(url).build()
        var logoUrl: String = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val logoJSON = JSONObject(response.body()!!.string())
                    logoUrl = logoJSON.getString("url")
                    stock.setLogoUrl(logoUrl)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })

        return logoUrl
    }

    fun getAllStocks(): ArrayList<Stock> {
        return this._stocks
    }

    fun setLiveStocks(stocks: ArrayList<Stock>) {
        _stocks = stocks
    }
    fun getLiveStocks(): LiveData<ArrayList<Stock>> {
        return _liveStocks
    }

    companion object {
        private val URL_SCHEME = "https"
        private val URL_AUTHORITY = "cloud.iexapis.com"
        private val URL_PATH_1 = "stable"
        private val URL_PATH_2 = "stock"
        private val URL_PATH_3 = "market"
        private val URL_PATH_4 = "collection"
        private val URL_PATH_5 = "list"
        private val URL_QUERY_PARAM_COLLECTION_NAME = "collectionName"
        private val URL_QUERY_PARAM_TOKEN = "token"
        private val URL_QUERY_LIMIT_COUNT = "listLimit"
    }
}