package com.mehmetpetek.themoviedb.presentation.home

import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.themoviedb.common.Constant
import com.mehmetpetek.themoviedb.common.extensions.isTablet
import com.mehmetpetek.themoviedb.common.extensions.visibleIf
import com.mehmetpetek.themoviedb.data.remote.model.Result
import com.mehmetpetek.themoviedb.databinding.FragmentHomeBinding
import com.mehmetpetek.themoviedb.domain.usecase.AllMoviesUseCase
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import com.mehmetpetek.themoviedb.presentation.common.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MovieAdapter.OnMovieListener {

    private val viewModel by viewModels<HomeViewModel>()
    override val saveBinding: Boolean = true
    private var firstMovieInitialize = false

    override fun bindScreen() {
        getEffect()
        getState()
    }

    private fun getEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    when (it) {
                        is HomeEffect.ShowError -> {
                            handleError(it.throwable)
                        }

                        is HomeEffect.GoToMovieDetail -> {
                            if (requireActivity().isTablet()) {
                                setFragmentResult(it.movieId)
                            } else {
                                findNavController().navigate(HomeFragmentDirections.homeToDetail(it.movieId))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    setLoadingState(it.isLoading)
                    if (!it.isLoading) {
                        it.allMovies.forEach { (key, value) ->
                            when (key) {
                                AllMoviesUseCase.MovieType.POPULARITY_DESC -> {
                                    if (!firstMovieInitialize) {
                                        firstMovieInitialize = true
                                        initialDetailFragment(value?.results?.first()?.id ?: 0)
                                    }
                                    setRecyclerview(
                                        binding.tvPopularityMoviesTitle,
                                        binding.rvPopularityMovies,
                                        MovieAdapterModel(getString(key.movieType), value)
                                    )
                                }

                                AllMoviesUseCase.MovieType.REVENUE_DESC -> {
                                    setRecyclerview(
                                        binding.tvRevenueMoviesTitle,
                                        binding.rvRevenueMovies,
                                        MovieAdapterModel(getString(key.movieType), value)
                                    )
                                }

                                AllMoviesUseCase.MovieType.PRIMARY_RELEASE_DATE_DESC -> {
                                    setRecyclerview(
                                        binding.tvPrimaryReleaseDateMoviesTitle,
                                        binding.rvPrimaryReleaseDateMovies,
                                        MovieAdapterModel(getString(key.movieType), value)
                                    )
                                }

                                AllMoviesUseCase.MovieType.VOTE_AVERAGE_DESC -> {
                                    setRecyclerview(
                                        binding.tvVoteAverageTitle,
                                        binding.rvVoteAverageMovies,
                                        MovieAdapterModel(getString(key.movieType), value)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerview(
        textView: TextView,
        recyclerView: RecyclerView,
        movieAdapterModel: MovieAdapterModel
    ) {
        textView.text = movieAdapterModel.title
        recyclerView.setHasFixedSize(true)
        if (recyclerView.adapter == null) {
            val adapter = MovieAdapter(this).apply {
                submitList(movieAdapterModel.results?.results)
            }
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(object :
                PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    viewModel.setEvent(HomeEvent.LoadMore(movieAdapterModel.title))
                }

                override fun isLastPage(): Boolean =
                    (recyclerView.adapter as MovieAdapter).itemCount >= (movieAdapterModel.results?.total_pages
                        ?: 0)

                override fun isLastedPage() {}
                override fun isNotLastedPage() {}
            })
        } else {
            (recyclerView.adapter as MovieAdapter).updateList(movieAdapterModel.results?.results as List<Result>)
        }
    }


    private fun initialDetailFragment(movieId: Int) {
        binding.detailContainer.visibleIf(requireActivity().isTablet())
        setFragmentResult(movieId)
    }

    private fun setFragmentResult(movieId: Int) {
        requireActivity().supportFragmentManager.setFragmentResult(
            Constant.DetailDataListener.DETAIL_SCREEN,
            bundleOf(
                Constant.DetailDataListener.DETAIL_MOVIE_ID to movieId
            )
        )
    }

    override fun onClickMovie(movieId: Int) {
        viewModel.setEvent(HomeEvent.OnClickMovieDetail(movieId))
    }
}