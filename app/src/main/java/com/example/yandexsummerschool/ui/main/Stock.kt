package com.example.yandexsummerschool.ui.main

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import com.example.yandexsummerschool.R
import org.json.JSONObject
import java.math.RoundingMode
import kotlin.math.absoluteValue

class Stock(stockJSON: JSONObject) {
    private lateinit var _logoBitmap: Bitmap
    private var _stockName: String = stockJSON.getString("symbol")
    private var _companyName: String = stockJSON.getString("companyName")
    private var _currentPrice: String
    private var _dayDelta: String

    private var _dayDeltaIsPositive: Boolean = true
    private var _deltaColor: Int

    private var _isFavourite: Boolean = false
    private var _chart = ArrayList<Float>()

    init {
        _currentPrice = "$" + stringTo2decimalDouble(stockJSON.getString("latestPrice")).toString()
        val change = stringTo2decimalDouble(stockJSON.getString("change"))
        val changePercent = stringTo2decimalDouble(stockJSON.getString("changePercent"))

        if (change >= 0.0) {
            _dayDelta = "$${change} (${changePercent}%)"
            _deltaColor = Color.parseColor("#B32424")
        } else {
            _dayDelta = "-$${change.absoluteValue} (${changePercent}%)"
            _deltaColor = Color.parseColor("#24B35D")
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
        return _stockName
    }

    fun getCompanyName(): String {
        return _companyName
    }

    fun getCurrentPrice(): String {
        return _currentPrice
    }

    fun getDayDelta(): String {
        return _dayDelta
    }

    fun getDeltaColor(): Int {
        return _deltaColor
    }

    fun setLogoBitmap(bitmap: Bitmap) {
        _logoBitmap = bitmap
    }

    fun getLogoBitmap(): Bitmap {
        return _logoBitmap
    }

    fun isDayDeltaPositive(): Boolean {
        return _dayDeltaIsPositive
    }

    fun setChart(chart: ArrayList<Float>) {
        _chart = chart
    }

    fun getChart(): ArrayList<Float> {
        return _chart
    }
}