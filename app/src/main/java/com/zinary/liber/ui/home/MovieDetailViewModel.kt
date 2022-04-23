package com.zinary.liber.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.models.*
import com.zinary.liber.repo.MoviesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val moviesRepo by lazy { MoviesRepo() }

    var movie = MutableLiveData<Movie>()
    var castList = MutableLiveData<List<Cast>>()
    var crewList = MutableLiveData<List<Crew>>()

    var videoList = MutableLiveData<List<Video>>()
    var apiError = MutableLiveData<String>()

    fun getMovieDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getMovieDetails(id)
            if (response.isSuccessful) {
                val movieResponse = response.body()
                if (movieResponse != null) {
                    movie.postValue(movieResponse!!)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@MovieDetailViewModel.javaClass.name, error)
            }
        }
    }

    fun getCredits(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getCredits(id)
            if (response.isSuccessful) {
                val creditsResponse = response.body()
                if (creditsResponse != null) {
                    castList.postValue(creditsResponse.cast)
                    crewList.postValue(creditsResponse.crew)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@MovieDetailViewModel.javaClass.name, error)
            }
        }
    }

    fun getVideos(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getVideos(id)
            if (response.isSuccessful) {
                val videoResponse = response.body()
                if (videoResponse != null) {
                    videoList.postValue(videoResponse.results)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@MovieDetailViewModel.javaClass.name, error)
            }
        }
    }

}