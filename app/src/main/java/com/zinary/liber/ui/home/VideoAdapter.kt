package com.zinary.liber.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zinary.liber.databinding.VideoItemBinding
import com.zinary.liber.models.Video


class VideoAdapter(private val context: Context, private var castList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.MovieViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setVideoList(movies: List<Video>) {
        castList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val itemBinding: VideoItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(video: Video) {
            if (video.site == "YouTube") {
                val thumbnailUrl = "https://img.youtube.com/vi/${video.key}/maxresdefault.jpg"
                val videoUrl = "https://www.youtube.com/watch?v=${video.key}"

                Glide.with(context)
                    .load(thumbnailUrl)
                    .into(itemBinding.thumbnail)

                itemBinding.videoName.text = video.name
                itemBinding.thumbnail.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(videoUrl)
                    context.startActivity(intent, null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val videoItemBinding =
            VideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(videoItemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    override fun getItemCount(): Int = castList.size
}