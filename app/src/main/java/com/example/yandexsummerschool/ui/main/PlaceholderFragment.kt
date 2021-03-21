package com.example.yandexsummerschool.ui.main

//import okhttp3.*
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


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(favouriteStockStore : FavouriteStockStore, stockRequester: StockRequester) : Fragment() {

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var pageViewModel: PageViewModel
    private val _favouriteStockStore = favouriteStockStore
    private val _stockRequester: StockRequester = stockRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageType = PageType.apply(arguments?.getInt(ARG_SECTION_NUMBER) ?: 0)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setPageType(pageType)
        }
        adapter = RecyclerViewAdapter (){
            _favouriteStockStore.onFavouriteChange(it)
            pageViewModel.restore(_stockRequester, _favouriteStockStore)
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
        //pageViewModel.liveStocks.observe(this, Observer(_stockRequester::setLiveStocks))
        pageViewModel.restore(_stockRequester, _favouriteStockStore)
    }

    override fun onResume() {
        super.onResume()
        pageViewModel.restore(_stockRequester, _favouriteStockStore)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pageType: PageType, stockRequester: StockRequester, favouriteStockStore : FavouriteStockStore): PlaceholderFragment {
            return PlaceholderFragment(favouriteStockStore, stockRequester).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, pageType.value)
                }
            }
        }
    }
}