package com.zinary.liber.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ImageItemBinding
import com.zinary.liber.models.Image
import com.zinary.liber.utils.loadFromUrl


class ImagesAdapter(private val context: Context, private var imageList: List<Image>) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val itemBinding: ImageItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: Image) {
            val imagePath: String = Constants.BASE_IMAGE_URL + image.filePath
            itemBinding.imageView.loadFromUrl(imagePath, context)
            itemBinding.imageView.setOnClickListener {
                val intent = Intent(context, ImagePreviewActivity::class.java)
                val urls = imageList.map { it.filePath.orEmpty() } as ArrayList<String>
                intent.putStringArrayListExtra("IMAGE_URLS", urls)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageItemBinding =
            ImageItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(imageItemBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.count()
}