package com.zinary.liber

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.enums.MoviesType
import com.zinary.liber.models.Genres
import com.zinary.liber.models.Movie
import com.zinary.liber.repo.MoviesRepo
import com.zinary.liber.ui.search.SearchPageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {
    private val moviesRepo by lazy { MoviesRepo() }

    var upcomingMovies = MutableLiveData<List<Movie>>()
    var topRatedMovies = MutableLiveData<List<Movie>>()
    var nowPlayingMovies = MutableLiveData<List<Movie>>()
    var popularMovies = MutableLiveData<List<Movie>>()

    var apiError = MutableLiveData<String>()
    var genreList = MutableLiveData<List<Genres>>()
    var searchQuery = "batman"

    fun getMovies(type: MoviesType, movieList: MutableLiveData<List<Movie>>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = moviesRepo.getMovies(type)
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    if (movieResponse != null) {
                        movieList.postValue(movieResponse.results.filter { it.posterPath != null })
                    }
                } else {
                    val error = response.errorBody()?.string().toString()
                    apiError.postValue(error)
                    Log.e(this@MainViewModel.javaClass.name, error)
                }
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message, e)
            } catch (e : IOException) {
                apiError.postValue(e.message)
            }
        }
    }

    fun searchMovies(query: String): LiveData<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            SearchPageSource(query)
        }.liveData.cachedIn(viewModelScope)
    }


    fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message, e)
            }  catch (e : IOException) {
                apiError.postValue(e.message)
            }
        }
    }

}