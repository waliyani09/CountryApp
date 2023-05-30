package com.example.sampleproject.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleproject.databinding.ActivityMainBinding
import com.example.sampleproject.ui.adapters.CountriesRecyclerAdapter
import com.example.sampleproject.ui.viewmodels.MainViewModel

/**
 * Parent Actiivty that will show the list of countries in RecycleView
 */
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var countryAdapter: CountriesRecyclerAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setUpViewModel()
        setUpRecyclerView()

        mainViewModel.responseContainer.observe(this, Observer {
            if (it != null) {
                countryAdapter.result = it
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })

        mainViewModel.isShowProgress.observe(this, Observer {
            if (it) {
                activityMainBinding.mainProgressBar.visibility = View.VISIBLE
            } else {
                activityMainBinding.mainProgressBar.visibility = View.GONE
            }
        })

        mainViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpRecyclerView() = activityMainBinding.countryRecyclerView.apply {
        countryAdapter = CountriesRecyclerAdapter()
        adapter = countryAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun setUpViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getCountriesFromAPI()
    }
}