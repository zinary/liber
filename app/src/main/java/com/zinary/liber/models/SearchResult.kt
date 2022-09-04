package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val id: Int,
    val name: String?,
    val title: String?,
    val type: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("poster_path") val posterPath: String?,
)
