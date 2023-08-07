package com.mehmetpetek.themoviedb.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mehmetpetek.themoviedb.BuildConfig
import com.mehmetpetek.themoviedb.data.remote.model.Result
import com.mehmetpetek.themoviedb.databinding.RvMovieBinding

class MovieAdapter(
    private val onMovieListener: OnMovieListener
) : ListAdapter<Result, MovieAdapter.MovieViewHolder>(MovieDiffUtil()) {

    class MovieDiffUtil : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            RvMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onMovieListener
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    class MovieViewHolder(
        private val binding: RvMovieBinding,
        private val onMovieListener: OnMovieListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            result: Result
        ) {
            binding.ivMovieImage.load("${BuildConfig.IMAGE_BASE_URL}${result.poster_path}")

            binding.root.setOnClickListener {
                onMovieListener.onClickMovie(result)
            }
        }
    }

    interface OnMovieListener {
        fun onClickMovie(result: Result)
    }
}