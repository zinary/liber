package com.zinary.liber.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.chip.Chip
import com.zinary.liber.Liber
import com.zinary.liber.R


object Utils {
    private val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.8f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .setHeightRatio(2f)
            .setWidthRatio(1f)
            .build()

    // This is the placeholder for the imageView
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    fun createExternalIdChip(
        context: Context,
        name: String,
        url: String,
        iconDrawableId: Int? = null
    ): Chip {
        val chip = Chip(context).apply {
            text = name
            chipBackgroundColor = context.getColorStateList(R.color.black)
            elevation = 20f
            chipIconSize = 50f
            iconStartPadding = 20f
            chipIcon = context.getDrawable(iconDrawableId ?: R.drawable.ic_baseline_settings_24)
            setTextColor(context.getColor(R.color.white))
            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }

        return chip
    }

    fun downloadFile(url: String) {
        val downloadManager =
            Liber.getContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val request = DownloadManager.Request(uri)
        request.setTitle("Liber")
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadManager?.enqueue(request)
    }
}