package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val page: Int = 0,
    val results: List<Review>,
    @SerializedName("total_pages") var totalPages: Int,
)
