package com.example.sampleproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleproject.databinding.RawCountriesItemLayoutBinding
import com.example.sampleproject.network.model.Country
import com.example.sampleproject.utils.Const.Companion.COMMA_SEPARATOR

/**
 * List adapter for Countries List
 */
class CountriesRecyclerAdapter :
    RecyclerView.Adapter<CountriesRecyclerAdapter.CountriesViewHolder>() {

    inner class CountriesViewHolder(private val rawCountriesItemLayoutBinding: RawCountriesItemLayoutBinding) :
        RecyclerView.ViewHolder(rawCountriesItemLayoutBinding.root) {

        fun bind(results: Country) {
            rawCountriesItemLayoutBinding.apply {
                nameOfCountry.text = results.name + COMMA_SEPARATOR
                regionOfCountry.text = results.region
                codeOfCountry.text = results.code
                capitalOfCountry.text = results.capital
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var result: List<Country>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(
            RawCountriesItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(result[position])
    }

    override fun getItemCount() = result.size

}