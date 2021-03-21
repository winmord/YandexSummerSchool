package com.example.yandexsummerschool.ui.main

//import okhttp3.*
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.yandexsummerschool.R

private val TAB_TITLES = arrayOf(
    R.string.stocks,
    R.string.favourite
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val _stockRequester = StockRequester()
    private val _favouriteStockStore = FavouriteStockStore()

    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(PageType.apply(position), _stockRequester, _favouriteStockStore)
    }

    override fun getPageTitle(position: Int): CharSequence =
        context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 2
}