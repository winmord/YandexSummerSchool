package com.example.yandexsummerschool

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexsummerschool.ui.main.FavouriteStockStore
import com.example.yandexsummerschool.ui.main.MyApp.Companion.app
import com.example.yandexsummerschool.ui.main.RecyclerViewAdapter
import com.example.yandexsummerschool.ui.main.Stock
import com.example.yandexsummerschool.ui.main.StockRequester
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    private lateinit var _favouriteStockStore: FavouriteStockStore
    private lateinit var _stockApi: StockRequester
    private lateinit var _adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favouriteStockStore = app.favouriteStockStore
        _stockApi = app.stockRequester

        setContentView(R.layout.activity_search)
        val searchView = findViewById<SearchView>(R.id.new_search_view)

        val stocks = _stockApi.getMostActiveStocks().value

        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(this)
        _adapter = RecyclerViewAdapter() {
            _favouriteStockStore.onFavouriteChange(it)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    _adapter.setStocks(ArrayList<Stock>())
                } else {
                    _adapter.setStocks(stocks!!.filter {
                        it.getCompanyName().toLowerCase(Locale.ROOT)
                            .contains(newText.toLowerCase(Locale.ROOT))
                                || it.getStockName().toLowerCase(Locale.ROOT)
                            .contains(newText.toLowerCase(Locale.ROOT))
                    })
                    recyclerView.adapter = _adapter
                }
                return false
            }
        })

        //searchView.requestFocus()
    }
}
