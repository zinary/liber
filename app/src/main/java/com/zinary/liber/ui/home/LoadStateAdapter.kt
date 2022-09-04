package com.zinary.liber.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.databinding.ItemLoadStateBinding
import java.io.IOException

class LoadStateAdapter(
    private val retry: () -> Unit
) : androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    inner class LoadStateViewHolder(private val itemBinding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(loadState: LoadState) {

            itemBinding.loadStateRetry.isVisible = loadState !is LoadState.Loading
            itemBinding.loadStateErrorMessage.isVisible = loadState !is LoadState.Loading
            itemBinding.loadStateProgress.isVisible = loadState is LoadState.Loading

            if (loadState is LoadState.Error) {
                if (loadState.error is IOException) {
                    itemBinding.loadStateErrorMessage.text = "Cannot connect to internet"
                }
            }

            itemBinding.loadStateRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }
}

