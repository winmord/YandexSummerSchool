package com.example.yandexsummerschool.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexsummerschool.R
import com.example.yandexsummerschool.ui.main.MyApp.Companion.app

class PlaceholderFragment: Fragment() {

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageType = PageType.apply(arguments?.getInt(ARG_SECTION_NUMBER) ?: 0)
        val context = requireContext()
        val stockApi = context.app.stockRequester
        val favouriteStockStore = context.app.favouriteStockStore

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setPageType(pageType)
            setStores(favouriteStockStore, stockApi)
        }

        adapter = RecyclerViewAdapter() {
            favouriteStockStore.onFavouriteChange(it)
            pageViewModel.restore()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        pageViewModel.stocks.observe(this, Observer(adapter::setStocks))
        pageViewModel.logos.observe(this, Observer(adapter::setLogos))
        pageViewModel.restore()
    }

    override fun onResume() {
        super.onResume()
        pageViewModel.restore()
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(
            pageType: PageType
        ): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, pageType.value)
                }
            }
        }
    }
}