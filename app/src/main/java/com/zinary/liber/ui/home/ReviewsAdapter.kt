package com.zinary.liber.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ItemReviewBinding
import com.zinary.liber.models.Review
import com.zinary.liber.utils.loadFromUrl

class ReviewsAdapter : PagingDataAdapter<Review, ReviewsAdapter.ReviewViewHolder>(ReviewDiffer) {

    object ReviewDiffer : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    inner class ReviewViewHolder(private val itemBinding: ItemReviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(review: Review) {

            itemBinding.authorProfile.loadFromUrl(
                Constants.BASE_IMAGE_URL + review.authorDetails?.avatarPath,
                itemBinding.authorProfile.context
            )

            itemBinding.authorName.text = review.author
            itemBinding.crtAtDate.text = review.createdAt
            itemBinding.ratingBar.rating = review.authorDetails?.rating?.toFloat() ?: 0f
            var reviewClicked = false
            itemBinding.review.apply {
                text = review.content?.parseAsHtml()?.ifBlank { "Unavailable" }
                setOnClickListener {
                    reviewClicked = !reviewClicked
                    maxLines = if (reviewClicked) Int.MAX_VALUE else 5
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemReviewBinding =
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(itemReviewBinding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}