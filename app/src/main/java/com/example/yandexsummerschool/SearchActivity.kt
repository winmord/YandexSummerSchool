package com.example.yandexsummerschool

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.RequiresApi

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchView = findViewById<SearchView>(R.id.new_search_view)
        searchView.requestFocus()
    }
}