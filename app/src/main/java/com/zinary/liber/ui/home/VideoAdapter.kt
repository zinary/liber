package com.zinary.liber.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.databinding.VideoItemBinding
import com.zinary.liber.models.Video

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.MovieViewHolder>() {
    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    })

    fun setVideoList(movies: List<Video>) {
        differ.submitList(movies)
    }

    inner class MovieViewHolder(private val itemBinding: VideoItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(video: Video) {
            if (video.site == "YouTube") {
                val videoUrl = "https://www.youtube.com/embed/${video.key}"
                itemBinding.webView.setBackgroundColor(Color.TRANSPARENT)
                itemBinding.webView.settings.javaScriptEnabled = true
                itemBinding.webView.isSoundEffectsEnabled = true

                itemBinding.webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        itemBinding.progressCircular.setProgress(newProgress, true)
                        itemBinding.progressCircular.isVisible = newProgress < 100
                        super.onProgressChanged(view, newProgress)
                    }
                }

                itemBinding.webView.loadUrl(videoUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val videoItemBinding =
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(videoItemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.count()
}