package com.zinary.liber.models

data class MovieImagesResponse(
    val id: String,
    val backdrops: List<Image>,
    val posters: List<Image>
)