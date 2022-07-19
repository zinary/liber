package com.zinary.liber.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.MovieImageItemBinding
import com.zinary.liber.databinding.MoviePosterItemBinding
import com.zinary.liber.models.Image
import com.zinary.liber.utils.loadFromUrl


class MovieImageAdapter(private val onImageClickListener: OnImageClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val poster = 1
    private val backdrop = 2

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    })

    fun setMovieImageList(images: List<Image>) {
        differ.submitList(images)
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].aspectRatio < 1) poster else backdrop
    }

    inner class MoviePosterViewHolder(private val itemBinding: MoviePosterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: Image) {
            itemBinding.imageView.apply {
                loadFromUrl(Constants.BASE_IMAGE_URL + image.filePath, context)
                setOnClickListener {
                    val urls = differ.currentList.map { it.filePath.orEmpty() } as ArrayList<String>
                    onImageClickListener.onImageClicked(layoutPosition, urls)
                }
            }
        }
    }

    inner class MovieBackdropViewHolder(private val itemBinding: MovieImageItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: Image) {
            itemBinding.imageView.apply {
                loadFromUrl(Constants.BASE_IMAGE_URL + image.filePath, context)
                setOnClickListener {
                    val urls = differ.currentList.map { it.filePath.orEmpty() } as ArrayList<String>
                    onImageClickListener.onImageClicked(layoutPosition, urls)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == poster) {
            val imageItemBinding =
                MoviePosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MoviePosterViewHolder(imageItemBinding)
        } else {
            val imageItemBinding =
                MovieImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieBackdropViewHolder(imageItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MoviePosterViewHolder) {
            holder.bind(differ.currentList[position])
        }
        if (holder is MovieBackdropViewHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int = differ.currentList.count()

    interface OnImageClickListener {
        fun onImageClicked(position: Int, urls: ArrayList<String>)
    }
}