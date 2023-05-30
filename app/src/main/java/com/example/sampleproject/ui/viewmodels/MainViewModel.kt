package com.example.sampleproject.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleproject.network.RetrofitInstanceModule
import com.example.sampleproject.network.model.Country
import kotlinx.coroutines.*

/**
 * View model class with one function for API calling getCountriesFromAPI()
 */
class MainViewModel : ViewModel() {
    val responseContainer = MutableLiveData<ArrayList<Country>>()
    val errorMessage = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    private val countryService = RetrofitInstanceModule().theRetrofitInstance()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled : ${throwable.localizedMessage}")
    }

    fun getCountriesFromAPI() {
        isShowProgress.value = true
        job = viewModelScope.launch(exceptionHandler) {
            val response = countryService.getCountries()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    isShowProgress.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
