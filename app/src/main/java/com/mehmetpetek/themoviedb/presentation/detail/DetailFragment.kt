package com.mehmetpetek.themoviedb.presentation.detail

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.mehmetpetek.themoviedb.BuildConfig
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieImageResponse
import com.mehmetpetek.themoviedb.databinding.FragmentDetailBinding
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel by viewModels<DetailViewModel>()

    override fun bindScreen() {
        getEffect()
        getState()
    }

    private fun getEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.effect.collect {
                    when (it) {
                        else -> {}
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
                        it.movieImageDetail?.let { movieImage ->
                            setImages(movieImage)
                        }
                        it.movieDetail?.let { movieDetail ->
                            setDesc(movieDetail)
                        }
                    }
                }
            }
        }
    }

    private fun setImages(movieImage: MovieImageResponse) {
        if (movieImage.backdrops.isNotEmpty()) {
            binding.ivMovieBackdrop.load("${BuildConfig.IMAGE_BASE_URL}${movieImage.backdrops.first().file_path}") {
                error(R.drawable.ic_image)
                placeholder(R.drawable.ic_image)
                fallback(R.drawable.ic_image)
            }
        }
        if (movieImage.posters.isNotEmpty()) {
            binding.ivMoviePoster.load("${BuildConfig.IMAGE_BASE_URL}${movieImage.posters.first().file_path}") {
                error(R.drawable.ic_image)
                placeholder(R.drawable.ic_image)
                fallback(R.drawable.ic_image)
            }
        }
    }

    private fun setDesc(movieDetail: MovieDetailResponse) {
        binding.tvMovieTitle.text = movieDetail.title
        binding.tvMovieStarCount.text = movieDetail.vote_average.toString()
        binding.tvMovieDate.text = movieDetail.release_date
        binding.tvMovieDesc.text = movieDetail.overview
    }
}