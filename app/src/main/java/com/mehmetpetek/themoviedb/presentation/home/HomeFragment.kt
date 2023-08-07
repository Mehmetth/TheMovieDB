package com.mehmetpetek.themoviedb.presentation.home

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.Result
import com.mehmetpetek.themoviedb.databinding.FragmentHomeBinding
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import com.mehmetpetek.themoviedb.presentation.common.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MovieAdapter.OnMovieListener {

    private val viewModel by viewModels<HomeViewModel>()

    override fun bindScreen() {
        getEffect()
        getState()
    }

    private fun getEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.effect.collect {
                    when (it) {
                        is HomeEffect.ShowError -> {
                        }
                        is HomeEffect.GoToMovieDetail -> {
                            findNavController().navigate(HomeFragmentDirections.homeToDetail(it.movieId))
                        }
                    }
                }
            }
        }
    }

    private fun getState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    if (!it.isLoading) {
                        Log.d("CCCCCCCCCCCCCC","popularMovies : " + it.popularMovies?.results?.size)
                        Log.d("CCCCCCCCCCCCCC","topRatedMovies : " + it.topRatedMovies?.results?.size)
                        Log.d("CCCCCCCCCCCCCC","upcomingMovies : " + it.upcomingMovies?.results?.size)
                        Log.d("CCCCCCCCCCCCCC","nowPlayingMovies : " + it.nowPlayingMovies?.results?.size)
                        Log.d("CCCCCCCCCCCCCC","+++++++++++++++++++++++++++++")

                        it.popularMovies?.let { popularMovies ->
                            setRecyclerview(
                                binding.tvPopularMoviesTitle,
                                binding.rvPopularMovies,
                                AAAAA(getString(R.string.popular_movies), popularMovies)
                            )
                        }
                        it.topRatedMovies?.let { topRatedMovies ->
                            setRecyclerview(
                                binding.tvTopRatedMoviesTitle,
                                binding.rvTopRatedMovies,
                                AAAAA(getString(R.string.top_rated_movies), topRatedMovies)
                            )
                        }
                        it.upcomingMovies?.let { upcomingMovies ->
                            setRecyclerview(
                                binding.tvUpComingMoviesTitle,
                                binding.rvUpComingMovies,
                                AAAAA(getString(R.string.up_coming_movies), upcomingMovies)
                            )
                        }
                        it.nowPlayingMovies?.let { nowPlayingMovies ->
                            setRecyclerview(
                                binding.tvNowPlayingMoviesTitle,
                                binding.rvNowPlayingMovies,
                                AAAAA(getString(R.string.now_playing_movies), nowPlayingMovies)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerview(
        textView: TextView,
        recyclerView: RecyclerView,
        aaaa: AAAAA
    ) {
        textView.text = aaaa.title
        recyclerView.setHasFixedSize(true)
        if (recyclerView.adapter == null) {
            val adapter = MovieAdapter(this).apply {
                submitList(aaaa.results?.results)
            }
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(object :
                PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    viewModel.setEvent(HomeEvent.LoadMore(aaaa.title))
                }

                override fun isLastPage(): Boolean =
                    (recyclerView.adapter as MovieAdapter).itemCount >= (aaaa.results?.total_pages
                        ?: 0)

                override fun isLastedPage() {}
                override fun isNotLastedPage() {}
            })
        } else {
            val currentList =
                (recyclerView.adapter as MovieAdapter).currentList.distinct()
                    .toMutableList()
            currentList.addAll(aaaa.results?.results as List<Result>)
            (recyclerView.adapter as MovieAdapter).submitList(currentList)
        }
    }

    override fun onClickMovie(movieId: Int) {
        viewModel.setEvent(HomeEvent.OnClickMovieDetail(movieId))
    }
}