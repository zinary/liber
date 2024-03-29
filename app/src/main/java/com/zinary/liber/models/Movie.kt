package com.zinary.liber.models


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genres>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanies>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountries>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("videos") val videos: VideoResponse,
    @SerializedName("credits") val credits: Credits,
    @SerializedName("images") val images: MovieImagesResponse,
    @SerializedName("recommendations") val recommendations: MovieResponse,
    @SerializedName("external_ids") val externalIDs: ExternalIDs,
    @SerializedName("reviews") val reviews: ReviewResponse,
    @SerializedName("watch/providers") val watchProviders: Map<String, Any>
)