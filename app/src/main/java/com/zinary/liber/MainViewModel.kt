package com.zinary.liber

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.enums.MoviesType
import com.zinary.liber.models.Genres
import com.zinary.liber.models.Movie
import com.zinary.liber.repo.MoviesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val moviesRepo by lazy { MoviesRepo() }

    var movies = MutableLiveData<List<Movie>>()
    var featuredMovies = MutableLiveData<List<Movie>>()
    var apiError = MutableLiveData<String>()
    var genreList = MutableLiveData<List<Genres>>()

    fun getMovies(type: MoviesType, movieList: MutableLiveData<List<Movie>>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = moviesRepo.getMovies(type)
            if (response.isSuccessful) {
                val movieResponse = response.body()
                if (movieResponse != null) {
                    movieList.postValue(movieResponse.results)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@MainViewModel.javaClass.name, error)
            }
        }
    }


    fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.moviesAPI.getGenres()
            if (response.isSuccessful) {
                val genresResponse = response.body()
                if (genresResponse != null) {
                    genreList.postValue(genresResponse.genres)
                }
            } else {
                val error = response.errorBody()?.string().toString()
                apiError.postValue(error)
                Log.e(this@MainViewModel.javaClass.name, error)
            }
        }
    }

}