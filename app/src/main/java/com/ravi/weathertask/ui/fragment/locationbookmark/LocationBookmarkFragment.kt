package com.ravi.weathertask.ui.fragment.locationbookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ravi.weathertask.R
import com.ravi.weathertask.ui.MainActivityViewModel
import com.ravi.weathertask.databinding.FragmentLocationBookmarkBinding
import com.ravi.weathertask.ui.fragment.locationbookmark.adapter.BookmarkAdapter
import com.ravi.weathertask.utils.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LocationBookmarkFragment : Fragment(),DeleteBookmarkCallback {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var fragmentLocationBookmarkBinding: FragmentLocationBookmarkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLocationBookmarkBinding=  FragmentLocationBookmarkBinding.inflate(inflater, container, false)
        return fragmentLocationBookmarkBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityViewModel  = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        lifecycleScope.launchWhenStarted {

            mainActivityViewModel.getLocations()


            fragmentLocationBookmarkBinding.rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            val linearLayoutManager = LinearLayoutManager(activity)
            fragmentLocationBookmarkBinding.rv.layoutManager = linearLayoutManager

            activity?.let {

                mainActivityViewModel.bookmarkEmpty.observe(it,{ flag ->
                    fragmentLocationBookmarkBinding.bookmarkEmpty.isVisible = flag
                    fragmentLocationBookmarkBinding.rv.isVisible = !flag
                })

                mainActivityViewModel.cityListMutableLiveData.observe(it, { cityList ->
                    val bookmarkAdapter = BookmarkAdapter(cityList as MutableList,this@LocationBookmarkFragment)
                    fragmentLocationBookmarkBinding.rv.adapter = bookmarkAdapter
                })

                val swipeHandler = object : SwipeToDeleteCallback(it) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = fragmentLocationBookmarkBinding.rv.adapter as BookmarkAdapter
                        adapter.removeAt(viewHolder.adapterPosition)
                    }
                }

                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(fragmentLocationBookmarkBinding.rv)
            }


        }
    }

    override fun bookmarkId(id: Int?) {
        id?.let {
            mainActivityViewModel.deleteBookmark(it)
        }
    }

    override fun bookmarkItemClick(lat: String, lng: String) {
       val action= LocationBookmarkFragmentDirections.actionLocationBookmarkFragmentToCityFragment(lat,lng)
        Navigation.findNavController(fragmentLocationBookmarkBinding.root).navigate(action)
    }

}