package com.example.yandexsummerschool

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.yandexsummerschool.ui.main.MyApp.Companion.app
import com.example.yandexsummerschool.ui.main.SectionsPagerAdapter
import com.example.yandexsummerschool.ui.main.StockRequester
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var tabs: TabLayout
    private lateinit var _stockApi: StockRequester

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        _stockApi = app.stockRequester

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {
            _stockApi.update()
            Handler().postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }

        swipeRefreshLayout.requestFocus()
        val searchView: SearchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.requestFocus()
    }

    override fun onStart() {
        super.onStart()
        app.stockRequester.getStocks()
    }
}