package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val page: Int = 0,
    val results: List<SearchResult>,
    @SerializedName("total_pages") var totalPages: Int,
)
