package com.example.yandexsummerschool

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.example.yandexsummerschool.ui.main.FavouriteStockStore
import com.example.yandexsummerschool.ui.main.MyApp.Companion.app
import com.example.yandexsummerschool.ui.main.StockRequester
import ir.farshid_roohi.linegraph.ChartEntity
import ir.farshid_roohi.linegraph.LineChart
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt

class InfoActivity : AppCompatActivity() {
    private lateinit var _stockApi: StockRequester
    private lateinit var _favouriteStockStore: FavouriteStockStore

    private var buttonFavState = false
    private val graph1 = floatArrayOf(
        113000f,
        183000f,
        188000f,
        695000f,
        324000f,
        230000f,
        188000f,
        15000f,
        126000f,
        5000f,
        33000f
    )


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        _stockApi = app.stockRequester
        _favouriteStockStore = app.favouriteStockStore

        val stockNameView: TextView = findViewById(R.id.infoStockName)
        val companyNameView: TextView = findViewById(R.id.infoCompanyName)
        val priceView: TextView = findViewById(R.id.infoStockPrice)
        val changeView: TextView = findViewById(R.id.infoStockChange)
        val buyButton: Button = findViewById(R.id.infoBuyButton)
        val backButton: Button = findViewById(R.id.infoBackButton)
        val favButton: Button = findViewById(R.id.infoFavButton)

        val adapterPosition = intent.getIntExtra("adapterPosition", 0)
        val stockIsFavourite = intent.getBooleanExtra("isFavourite", false)

        val stock =
            if (stockIsFavourite)
                _favouriteStockStore.getAllFavourite().value!![adapterPosition]
            else
                _stockApi.getMostActiveStocks().value!![adapterPosition]

        stockNameView.text = stock.getStockName()
        companyNameView.text = stock.getCompanyName()
        priceView.text = stock.getCurrentPrice()
        changeView.text = stock.getDayDelta()
        changeView.setTextColor(stock.getDeltaColor())

        val lineChart: LineChart = findViewById(R.id.graph)

        val rand = Random()
        val chart = List(30) { rand.nextFloat() * 10000 }

        val firstChartEntity = ChartEntity(Color.BLACK, chart.toFloatArray())

        val list = ArrayList<ChartEntity>()
        list.add(firstChartEntity)
        lineChart.setList(list)
        lineChart.bgColor = Color.WHITE

        buyButton.text = "BUY FOR ${stock.getCurrentPrice()}"

        backButton.setOnClickListener {
            this.finish()
        }

        buttonFavState = stock.isFavourite()
        favButton.setOnClickListener {
            if (buttonFavState) {
                favButton.foreground =
                    AppCompatResources.getDrawable(this, android.R.drawable.btn_star_big_off)
                buttonFavState = false
            } else {
                favButton.foreground =
                    AppCompatResources.getDrawable(this, android.R.drawable.btn_star_big_on)
                buttonFavState = true
            }

            stock.setFavourite(buttonFavState)
            _favouriteStockStore.onFavouriteChange(stock)
        }

        buyButton.setOnClickListener {
            Toast.makeText(this, "Stonk!", Toast.LENGTH_SHORT).show()
        }
    }
}