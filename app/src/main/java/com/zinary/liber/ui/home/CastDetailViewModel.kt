package com.zinary.liber.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.models.Image
import com.zinary.liber.models.Person
import com.zinary.liber.models.PersonCredits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CastDetailViewModel : ViewModel() {
    var person = MutableLiveData<Person>()
    var images = MutableLiveData<List<Image>>()
    var personCredits = MutableLiveData<PersonCredits>()
    var apiError = MutableLiveData<String>()

    fun getPersonDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getPersonDetails(id)
            if (response.isSuccessful) {
                val personResponse = response.body()
                if (personResponse != null) {
                    person.postValue(personResponse)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@CastDetailViewModel.javaClass.name, error)
            }
        }
    }

    fun getPersonImages(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getPersonImages(id)
            if (response.isSuccessful) {
                val imagesResponse = response.body()
                if (imagesResponse != null) {
                    images.postValue(imagesResponse.results)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@CastDetailViewModel.javaClass.name, error)
            }
        }
    }

    fun getPersonMovies(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getPersonMovies(id)
            if (response.isSuccessful) {
                val moviesResponse = response.body()
                if (moviesResponse != null) {
                    personCredits.postValue(moviesResponse)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@CastDetailViewModel.javaClass.name, error)
            }
        }
    }
}