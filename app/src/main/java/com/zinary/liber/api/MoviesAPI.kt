package com.zinary.liber.api

import com.zinary.liber.BuildConfig
import com.zinary.liber.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {
    @GET("movie/{movie_id}?api_key=${BuildConfig.TMDB_API_KEY}&append_to_response=videos,credits,images,recommendations,external_ids,watch/providers,reviews")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Response<Movie>

    @GET("movie/{type}?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getMovies(
        @Path("type") type: String,
        @Query("region") region: String,
        @Query("adult") adult: Boolean,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("person/{person_id}?api_key=${BuildConfig.TMDB_API_KEY}&append_to_response=images,movie_credits,external_ids")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
    ): Response<Person>

    @GET("movie/{movie_id}/reviews?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getMovieReviews(
        @Path("movie_id") movie_id: Int,
        @Query("page") page: Int
    ): Response<ReviewResponse>


    @GET("person/{person_id}/movie_credits?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getPersonMovies(
        @Path("person_id") personId: Int,
    ): Response<PersonCredits>

    @GET("search/multi?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Response<SearchResponse>

    @GET("genre/movie/list?api_key=${BuildConfig.TMDB_API_KEY}&language=en-US&page=1")
    suspend fun getGenres(): Response<GenresResponse>

}