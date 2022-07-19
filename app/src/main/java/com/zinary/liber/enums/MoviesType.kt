package com.zinary.liber.enums

enum class MoviesType(val key: String, val title: String) {
    LATEST("latest", "Latest"),
    POPULAR("popular","Popular"),
    NOW_PLAYING("now_playing","Now Playing"),
    UPCOMING("upcoming", "Upcoming"),
    TOP_RATED("top_rated","Top Rated")
}