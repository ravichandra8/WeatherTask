package com.ravi.weathertask.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ravi.weathertask.databinding.FragmentSettingsBinding
import com.ravi.weathertask.ui.MainActivityViewModel
import com.ravi.weathertask.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var fragmentSettingsBinding:FragmentSettingsBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return fragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        if( Constants.units.contentEquals("metric")){
           fragmentSettingsBinding.rb1.isChecked = true
           fragmentSettingsBinding.rb2.isChecked = false
       } else{
           fragmentSettingsBinding.rb1.isChecked = false
           fragmentSettingsBinding.rb2.isChecked = true
       }

        fragmentSettingsBinding.rg.setOnCheckedChangeListener { radioGroup, i ->
           val radioButton =fragmentSettingsBinding.rg.findViewById<RadioButton>(i)
            Constants.units=radioButton.text.toString()
        }

        mainActivityViewModel.checkCityListEmpty()

        activity?.let {
            mainActivityViewModel.bookmarkEmpty.observe(it,{
                if(it){
                    fragmentSettingsBinding.reset.text = "bookmark cleared"
                } else {
                    fragmentSettingsBinding.reset.setOnClickListener {
                        lifecycleScope.launchWhenStarted {
                            withContext(Dispatchers.IO) {
                                mainActivityViewModel.deleteAllBookmarks()
                            }
                            withContext(Dispatchers.Main){
                                Snackbar.make(fragmentSettingsBinding.root,"Bookmarks cleared",Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
        }


    }
}