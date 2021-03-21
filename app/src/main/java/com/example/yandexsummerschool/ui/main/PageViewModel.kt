package com.example.yandexsummerschool.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    private val _liveData = MutableLiveData<ArrayList<Stock>>()
    private val _liveStocks = MutableLiveData<ArrayList<Stock>>()
    private val _restoreLiveData = MutableLiveData<Int>()
    private lateinit var _pageType: PageType
    private var isRequested = false
    private val _stockRequester = StockRequester()
    // private lateinit var _favouriteStockStore: FavouriteStockStore

    val stocks: LiveData<ArrayList<Stock>> = _liveData
    val liveStocks: LiveData<ArrayList<Stock>> = _liveStocks
    //val apiStocks: LiveData<ArrayList<Stock>> = _stockRequester.getAllStocks()

    fun setPageType(pageType: PageType) {
        _pageType = pageType
    }

    fun restore(stockRequester: StockRequester, favouriteStockStore: FavouriteStockStore) {
        _restoreLiveData.postValue(0)
        _liveData.value = when (_pageType) {
            AllStocksPage -> {
                //apiStocks.value
                //_stockApi.all().map{ it.favourite = _favouriteStockStore.check(it.id) }
                stockRequester.getAllStocks()
                //stockRequester.getLiveStocks().value
            }
            FavouriteStocksPage -> {
                //val favId: List<Int> = _favouriteStockStore.all()
                //_stockApi.withId(favId)
                favouriteStockStore.getFavouriteStocks()
            }
        }
    }
}
