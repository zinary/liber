package com.zinary.liber.utils

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.zinary.liber.R
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Loads the image view with the given image url by using glide.
 * @param url   the url of the image
 * @param context   context
 * @param cacheStrategy cache strategy for loading the image. Default DiskCacheStrategy.AUTOMATIC
 * @param skipMemoryCache to skip memory cache. Default false
 */
fun ImageView.loadFromUrl(
    url: String,
    context: Context,
    blur: Boolean = false
) {
    var glide = Glide.with(context)
        .load(url)
        .placeholder(ProgressBar(context).progressDrawable)
        .error(R.drawable.ic_baseline_settings_24)
    if (blur) {
        glide = glide.apply(bitmapTransform(BlurTransformation(25, 3)))

    }
    glide.into(this)
}
