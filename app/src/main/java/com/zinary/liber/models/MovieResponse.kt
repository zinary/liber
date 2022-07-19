package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int = 0,
    val results: List<Movie>,
    @SerializedName("total_pages") var totalPages: Int,
)
