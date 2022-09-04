package com.zinary.liber.enums

enum class MoviesType(val key: String, val title: String) {
    POPULAR("popular","Popular"),
    NOW_PLAYING("now_playing","Now in Theatres"),
    UPCOMING("upcoming", "Upcoming"),
    TOP_RATED("top_rated","Top Rated")
}