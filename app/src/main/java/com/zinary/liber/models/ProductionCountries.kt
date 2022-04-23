package com.zinary.liber.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductionCountries (
	@SerializedName("iso_3166_1") val iso_3166_1 : String,
	@SerializedName("name") val name : String
):Parcelable