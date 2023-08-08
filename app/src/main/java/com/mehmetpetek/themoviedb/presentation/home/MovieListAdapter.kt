package com.mehmetpetek.themoviedb.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.databinding.RvMovieListItemBinding

data class AAAAA(
    val title: String,
    val results: MovieResponse?
)

class MovieListAdapter(
    private val onMoviesListener: OnMoviesListener
) : ListAdapter<AAAAA, MovieListAdapter.MoviesViewHolder>(MovieDiffUtil()) {

    class MovieDiffUtil : DiffUtil.ItemCallback<AAAAA>() {
        override fun areItemsTheSame(oldItem: AAAAA, newItem: AAAAA) = oldItem == newItem
        override fun areContentsTheSame(oldItem: AAAAA, newItem: AAAAA) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            RvMovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onMoviesListener
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bindData(currentList[position], position)
    }

    class MoviesViewHolder(
        private val binding: RvMovieListItemBinding,
        private val onMoviesListener: OnMoviesListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            aaaa: AAAAA,
            position: Int
        ) {
            binding.tvMovieGroupTitle.text = aaaa.title
            /*
                        binding.rvMovies.setHasFixedSize(true)
                        val adapter = MovieAdapter().apply {
                            var asd: MutableList<String> = mutableListOf()
                            if (position == adapterPosition && binding.rvMovies.adapter != null) {
                                asd = (binding.rvMovies.adapter as MovieAdapter).currentList.toMutableList()
                            }
                            asd.addAll(aaaa.results?.results?.map { it.poster_path } as List<String>)
                            submitList(asd)
                        }
                        binding.rvMovies.adapter = adapter
                        */
        }
    }

    interface OnMoviesListener {
        fun onLoadMoreMovies(index: Int)
    }
}