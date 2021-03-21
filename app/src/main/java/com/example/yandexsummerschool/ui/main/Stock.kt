package com.example.yandexsummerschool.ui.main

import android.util.Log
import com.example.yandexsummerschool.R
//import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.math.RoundingMode
import java.util.*
import kotlin.math.absoluteValue

class Stock {
    private var imageResource: Int = 0
    private var stockName: String
    private var companyName: String
    private var currentPrice: String
    private var dayDelta: String
    private var dayDeltaIsPositive: Boolean = true
    private var logoUrl: String = ""
    var favourite: Boolean = false

    constructor(
        _imageResource: Int,
        _stock_name: String,
        _company_name: String,
        _current_price: String,
        _day_delta: String
    ) {
        this.imageResource = _imageResource
        this.stockName = _stock_name
        this.companyName = _company_name
        this.currentPrice = _current_price
        this.dayDelta = _day_delta
    }

    constructor(stockJSON: JSONObject) {
        this.stockName = stockJSON.getString("symbol")
        this.companyName = stockJSON.getString("companyName")
        this.currentPrice =
            "$" + stringTo2decimalDouble(stockJSON.getString("latestPrice")).toString()

        val change = stringTo2decimalDouble(stockJSON.getString("change"))
        val changePercent = stringTo2decimalDouble(stockJSON.getString("changePercent"))
        this.dayDeltaIsPositive = (change >= 0.0)

        this.dayDelta =
            if (dayDeltaIsPositive) "$${change} (${changePercent}%)" else "-$${change.absoluteValue} (${changePercent}%)"
    }

    private fun stringTo2decimalDouble(s: String): Double {
        val num = if (!s.equals(null)) s.toDouble() else 0.0
        return num.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    fun getImageResource(): Int {
        return this.imageResource
    }

    fun getStockName(): String {
        return this.stockName
    }

    fun getCompanyName(): String {
        return this.companyName
    }

    fun getCurrentPrice(): String {
        return this.currentPrice
    }

    fun getDayDelta(): String {
        return this.dayDelta
    }

    fun getLogoUrl(): String {
        return this.logoUrl
    }

    fun setLogoUrl(logoUrl: String) {
        this.logoUrl = logoUrl
    }

    fun isDayDeltaPositive(): Boolean {
        return this.dayDeltaIsPositive
    }

    companion object {
        private val STOCK_DESCRIPTION = "count"
    }
}