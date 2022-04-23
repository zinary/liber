package com.zinary.liber.models


import com.google.gson.annotations.SerializedName


data class VideoResponse(

    @SerializedName("id") var id: Int,
    @SerializedName("results") var results: List<Video>

)