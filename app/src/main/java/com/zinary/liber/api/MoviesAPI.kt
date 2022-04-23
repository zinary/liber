package com.zinary.liber.api

import com.zinary.liber.BuildConfig
import com.zinary.liber.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesAPI {
    @GET("movie/{movie_id}?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Response<Movie>

    @GET("movie/{type}?api_key=${BuildConfig.TMDB_API_KEY}&language=en-US&page=1&adult=false")
    suspend fun getMovies(
        @Path("type") type: String,
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/credits?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
    ): Response<CreditsResponse>

    @GET("movie/{movie_id}/videos?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
    ): Response<VideoResponse>

    @GET("movie/{movie_id}/watch/providers?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getWatchProviders(
        @Path("movie_id") movieId: Int,
    ): Response<VideoResponse>

    @GET("person/{person_id}?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
    ): Response<Person>

    @GET("genre/movie/list?api_key=${BuildConfig.TMDB_API_KEY}&language=en-US&page=1")
    suspend fun getGenres(): Response<GenresResponse>

}