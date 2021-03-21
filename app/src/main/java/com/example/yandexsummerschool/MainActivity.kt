package com.example.yandexsummerschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpannable
import androidx.viewpager.widget.ViewPager
import com.example.yandexsummerschool.ui.main.Stock
import com.example.yandexsummerschool.ui.main.SectionsPagerAdapter
import com.example.yandexsummerschool.ui.main.StockRequester
import com.google.android.material.tabs.TabLayout
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        //stockRequester = StockRequester(this)

        val searchView: SearchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus.equals(true)) {
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                }
                currentFocus?.clearFocus()
            }
        }
        )

        /*tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab.setText(tab.text.toSpannable().setSpan(Size(), 0, tab))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab.text.toSpannable().removeSpan()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })*/
    }
}
