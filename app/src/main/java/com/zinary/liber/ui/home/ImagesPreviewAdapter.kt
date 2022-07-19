package com.zinary.liber.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.GalleryImageSliderItemBinding
import com.zinary.liber.utils.loadFromUrl


class ImagesPreviewAdapter(private val context: Context, private var imageList: List<String>) :
    RecyclerView.Adapter<ImagesPreviewAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val itemBinding: GalleryImageSliderItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(imageUrl: String) {

            val imagePath: String = Constants.BASE_IMAGE_URL_ORIGINAL + imageUrl
            itemBinding.bigImage.loadFromUrl(imagePath, context)
            itemBinding.bigImage.positionAnimator.enter(false)
            itemBinding.bigImage.positionAnimator.addPositionUpdateListener { _, isLeaving ->
                if (isLeaving) {
                    (context as AppCompatActivity).finish()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageItemBinding =
            GalleryImageSliderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(imageItemBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.count()
}