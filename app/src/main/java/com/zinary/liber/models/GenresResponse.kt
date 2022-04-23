package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class GenresResponse(@SerializedName("genres") var genres: List<Genres>)
