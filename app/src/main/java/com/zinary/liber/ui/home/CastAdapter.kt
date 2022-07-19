package com.zinary.liber.ui.home

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.CastItemBinding
import com.zinary.liber.models.Cast
import com.zinary.liber.utils.loadFromUrl


class CastAdapter(private val context: Context, private var castList: List<Cast>) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setCastList(casts: List<Cast>) {
        castList = casts
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val itemBinding: CastItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(cast: Cast) {
            val imagePath: String = Constants.BASE_IMAGE_URL + cast.profilePath
            itemBinding.profileImage.loadFromUrl(imagePath, context, R.drawable.ic_user)
            itemBinding.profileName.text = cast.name
            itemBinding.characterName.text = cast.character
            itemBinding.profileImage.setOnClickListener {
                val intent = Intent(context, CastDetailActivity::class.java)
                intent.putExtra("CAST_ID", cast.id)

                val imagePair = Pair.create(
                    itemBinding.profileImage as View,
                    itemBinding.profileImage.transitionName
                )
                val namePair = Pair.create(
                    itemBinding.profileName as View,
                    itemBinding.profileName.transitionName
                )

                val transitionActivityOptions: ActivityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(
                        context as AppCompatActivity,
                        imagePair,
                        namePair
                    )
                context.startActivity(
                    intent,
                    transitionActivityOptions.toBundle()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val castItemBinding =
            CastItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CastViewHolder(castItemBinding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    override fun getItemCount(): Int = castList.count()
}