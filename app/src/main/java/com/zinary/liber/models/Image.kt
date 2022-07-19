package com.zinary.liber.models

import com.google.gson.annotations.SerializedName


data class Image(
    @SerializedName("aspect_ratio") var aspectRatio: Double,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("width") var width: Int? = null
)