package com.example.yandexsummerschool

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import ir.farshid_roohi.linegraph.ChartEntity
import ir.farshid_roohi.linegraph.LineChart
import java.util.*

class InfoActivity : AppCompatActivity() {
    private val graph1 = floatArrayOf(113000f, 183000f, 188000f, 695000f, 324000f, 230000f, 188000f, 15000f, 126000f, 5000f, 33000f)
    private val legendArr = arrayOf("05/21", "05/22", "05/23", "05/24", "05/25", "05/26", "05/27", "05/28", "05/29", "05/30", "05/31")

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val stockNameView: TextView = findViewById(R.id.infoStockName)
        val companyNameView: TextView = findViewById(R.id.infoCompanyName)
        val priceView: TextView = findViewById(R.id.infoStockPrice)
        val changeView: TextView = findViewById(R.id.infoStockChange)
        val buyButton: Button = findViewById(R.id.infoBuyButton)

        stockNameView.text = intent.getStringExtra("stockName")
        companyNameView.text = intent.getStringExtra("companyName")
        priceView.text = intent.getStringExtra("stockPrice")
        changeView.text = intent.getStringExtra("stockChange")

        if (changeView.text.contains('-')) {
            changeView.setTextColor(Color.parseColor("#B32424"))
        } else {
            changeView.setTextColor(Color.parseColor("#24B35D"))
        }

        val lineChart: LineChart = findViewById(R.id.graph)
        val firstChartEntity = ChartEntity(Color.BLACK, graph1)

        val list = ArrayList<ChartEntity>()
        list.add(firstChartEntity)
        lineChart.setList(list)
        lineChart.bgColor = Color.WHITE

        buyButton.text = "BUY FOR ${intent.getStringExtra("stockPrice")}"

        buyButton.setOnClickListener {
            Toast.makeText(this, "Stonk!", Toast.LENGTH_SHORT).show()
        }
    }
}