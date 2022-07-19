package com.zinary.liber.utils

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.zinary.liber.R
import jp.wasabeef.glide.transformations.BlurTransformation


fun ImageView.loadFromUrl(
    url: String,
    context: Context,
    errorDrawableId: Int? = null,
    blur: Boolean = false
) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(context.getColor(R.color.pink))
        start()
    }

    var glide = Glide.with(context)
        .load(url)
        .placeholder(circularProgressDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.movies)
    if (blur) {
        glide = glide.apply(bitmapTransform(BlurTransformation(25, 3)))
    }
    if (errorDrawableId != null) {
        glide = glide.error(errorDrawableId)
    }

    glide.into(this)
}
