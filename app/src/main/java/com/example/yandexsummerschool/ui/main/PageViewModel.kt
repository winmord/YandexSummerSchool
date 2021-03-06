package com.example.yandexsummerschool.ui.main

import androidx.lifecycle.*

class PageViewModel() : ViewModel() {
    private val _restoreLiveData = MutableLiveData<Int>()
    private lateinit var _pageType: PageType
    private lateinit var _favouriteStockStore: FavouriteStockStore
    private lateinit var _stockApi: StockRequester

    val stocks: LiveData<List<Stock>>
        get() = _restoreLiveData.switchMap {
            when (_pageType) {
                AllStocksPage -> _stockApi.getMostActiveStocks().map { items ->
                    items.map {
                        it
                    }
                }
                FavouriteStocksPage -> _favouriteStockStore.getAllFavourite().map { items ->
                    items.map {
                        it
                    }
                }
            }
        }

    val logos: LiveData<Int>
        get() = _restoreLiveData.switchMap {
            _stockApi.getLogos().map { items ->
                    it
            }
        }

    val charts: LiveData<Int>
        get() = _restoreLiveData.switchMap {
            _stockApi.getCharts().map { items ->
                it
            }
        }

    fun setStores(favouriteStockStore: FavouriteStockStore, stockApi: StockRequester) {
        _favouriteStockStore = favouriteStockStore
        _stockApi = stockApi
    }

    fun setPageType(pageType: PageType) {
        _pageType = pageType
    }

    fun restore() {
        _restoreLiveData.postValue(0)
    }
}
