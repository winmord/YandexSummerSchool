package com.example.yandexsummerschool.ui.main

sealed class PageType {

    abstract val value: Int

    companion object {
        fun apply(value: Int): PageType =
            when (value) {
                1 -> FavouriteStocksPage
                else -> AllStocksPage
            }
    }
}

object AllStocksPage : PageType() {
    override val value: Int
        get() = 0
}

object FavouriteStocksPage : PageType() {
    override val value: Int
        get() = 1
}
