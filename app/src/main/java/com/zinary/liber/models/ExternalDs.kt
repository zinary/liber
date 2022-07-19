package com.zinary.liber.models

import com.google.gson.annotations.SerializedName

data class ExternalIDs(

    @SerializedName("imdb_id") var imdbId: String? = null,
    @SerializedName("facebook_id") var facebookId: String? = null,
    @SerializedName("instagram_id") var instagramId: String? = null,
    @SerializedName("twitter_id") var twitterId: String? = null,
    @SerializedName("id") var id: Int? = null

)