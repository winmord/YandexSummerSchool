package com.example.yandexsummerschool.ui.main

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.example.yandexsummerschool.R
import org.json.JSONObject
import java.math.RoundingMode
import kotlin.math.absoluteValue

class Stock(stockJSON: JSONObject) {
    private var _stockName: String = stockJSON.getString("symbol")
    private var _companyName: String = stockJSON.getString("companyName")
    private var _currentPrice: String
    private var _dayDelta: String
    private var _dayDeltaIsPositive: Boolean = true
    private lateinit var _logoBitmap: Bitmap
    private var _isFavourite: Boolean = false
    private var _chart = ArrayList<Float>()

    init {
        _currentPrice = "$" + stringTo2decimalDouble(stockJSON.getString("latestPrice")).toString()
        val change = stringTo2decimalDouble(stockJSON.getString("change"))
        val changePercent = stringTo2decimalDouble(stockJSON.getString("changePercent"))
        _dayDeltaIsPositive = (change >= 0.0)
        _dayDelta =
            if (_dayDeltaIsPositive) {
                "$${change} (${changePercent}%)"
            } else {
                "-$${change.absoluteValue} (${changePercent}%)"
            }
    }

    private fun stringTo2decimalDouble(s: String): Double {
        val num = if (!s.equals(null)) s.toDouble() else 0.0
        return num.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    fun setFavourite(isFavourite: Boolean) {
        _isFavourite = isFavourite
    }

    fun isFavourite(): Boolean {
        return _isFavourite
    }

    fun getStockName(): String {
        return this._stockName
    }

    fun getCompanyName(): String {
        return this._companyName
    }

    fun getCurrentPrice(): String {
        return this._currentPrice
    }

    fun getDayDelta(): String {
        return this._dayDelta
    }

    fun setLogoBitmap(bitmap: Bitmap) {
        this._logoBitmap = bitmap
    }

    fun getLogoBitmap(): Bitmap {
        return this._logoBitmap
    }

    fun isDayDeltaPositive(): Boolean {
        return this._dayDeltaIsPositive
    }

    fun setChart(chart: ArrayList<Float>) {
        _chart = chart
    }

    fun getChart(): ArrayList<Float> {
        return _chart
    }
}