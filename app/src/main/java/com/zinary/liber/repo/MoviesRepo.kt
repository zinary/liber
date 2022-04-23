package com.zinary.liber.repo

import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.enums.MoviesType
import com.zinary.liber.models.Movie
import com.zinary.liber.models.MovieResponse
import retrofit2.Response

class MoviesRepo {
    suspend fun getMovieDetails(movieId: Int): Response<Movie> {
       return RetrofitInstance.moviesAPI.getMovieDetails(movieId)
    }

    suspend fun getMovies(movieType: MoviesType): Response<MovieResponse> {
       return RetrofitInstance.moviesAPI.getMovies(movieType.type)
    }


}