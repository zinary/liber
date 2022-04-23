package com.zinary.liber.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.CastItemBinding
import com.zinary.liber.models.Cast


class CastAdapter(private val context: Context, private var castList: List<Cast>) :
    RecyclerView.Adapter<CastAdapter.MovieViewHolder>() {

    fun setCastList(movies: List<Cast>) {
        castList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val itemBinding: CastItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(cast: Cast) {
            val imagePath = Constants.BASE_IMAGE_URL + cast.profilePath
            val placeholder = "https://st3.depositphotos.com/6672868/13701/v/600/depositphotos_137014128-stock-illustration-user-profile-icon.jpg"

            Glide.with(context)
                .load(if (cast.profilePath != null) imagePath else placeholder)
                .into(itemBinding.profileImage)

            itemBinding.profileName.text = cast.name
            itemBinding.characterName.text = cast.character
            itemBinding.profileImage.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val castItemBinding =
            CastItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(castItemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    override fun getItemCount(): Int = castList.size
}