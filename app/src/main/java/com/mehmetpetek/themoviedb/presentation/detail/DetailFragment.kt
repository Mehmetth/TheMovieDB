package com.mehmetpetek.themoviedb.presentation.detail

import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.dash.DashChunkSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.mehmetpetek.themoviedb.BuildConfig
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.common.Constant
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.DRM_LICENSE_URL
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.DRM_SCHEME
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.URL
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.USER_AGENT
import com.mehmetpetek.themoviedb.common.extensions.exitFullScreen
import com.mehmetpetek.themoviedb.common.extensions.fullScreen
import com.mehmetpetek.themoviedb.common.extensions.gone
import com.mehmetpetek.themoviedb.common.extensions.visible
import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieImageResponse
import com.mehmetpetek.themoviedb.databinding.FragmentDetailBinding
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private lateinit var playerView: ExoPlayer
    private var fullScreenVideo = false

    private val viewModel by viewModels<DetailViewModel>()

    override fun bindScreen() {
        getEffect()
        getState()

        initializePlayer()
        setUI()
        setFragmentResultListener()
    }

    private fun getEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.effect.collect {
                    when (it) {
                        is DetailEffect.ShowError -> {
                            handleError(it.throwable)
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
                    setLoadingState(it.isLoading)
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

    private fun setUI() {
        with(binding) {
            ivPlayBtn.setOnClickListener {
                it.gone()
                epPlayer.visible()
                clMovieAttr.gone()
                epPlayer.player?.playWhenReady = true
                ivCloseBtn.visible()
                ivFullScreenBtn.visible()
                ivFullScreenBtn.load(R.drawable.ic_fullscreen)
            }

            ivCloseBtn.setOnClickListener {
                releasePlayer()
                it.gone()
                binding.apply {
                    ivFullScreenBtn
                    ivFullScreenBtn.gone()
                    epPlayer.gone()
                    clMovieAttr.visible()
                    ivPlayBtn.visible()
                }
                initializePlayer()
            }

            ivFullScreenBtn.setOnClickListener {
                if (!fullScreenVideo) {
                    binding.ivFullScreenBtn.load(R.drawable.ic_fullscreen_exit)
                    requireActivity().fullScreen(binding.clTopArea)
                } else {
                    binding.ivFullScreenBtn.load(R.drawable.ic_fullscreen)
                    requireActivity().exitFullScreen(binding.clTopArea)
                }
                fullScreenVideo = !fullScreenVideo
            }
        }
    }

    private fun initializePlayer() {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(USER_AGENT)
            .setTransferListener(
                DefaultBandwidthMeter.Builder(requireContext())
                    .setResetOnNetworkTypeChange(false)
                    .build()
            )

        val dashChunkSourceFactory: DashChunkSource.Factory = DefaultDashChunkSource.Factory(
            defaultHttpDataSourceFactory
        )
        val manifestDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(USER_AGENT)
        val dashMediaSource =
            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(
                    MediaItem.Builder()
                        .setUri(Uri.parse(URL))
                        .setDrmConfiguration(
                            MediaItem.DrmConfiguration.Builder(DRM_SCHEME)
                                .setLicenseUri(DRM_LICENSE_URL).build()
                        )
                        .setMimeType(MimeTypes.APPLICATION_MPD)
                        .setTag(null)
                        .build()
                )

        ExoPlayer.Builder(requireContext())
            .setSeekForwardIncrementMs(10000)
            .setSeekBackIncrementMs(10000)
            .build().also { playerView = it }
        binding.epPlayer.player = playerView
        playerView.setMediaSource(dashMediaSource, true)
        playerView.prepare()
    }

    private fun releasePlayer() {
        binding.epPlayer.player?.let { player ->
            player.pause()
            player.release()
        }
    }

    private fun setImages(movieImage: MovieImageResponse) {
        with(binding) {
            if (movieImage.backdrops.isNotEmpty()) {
                ivMovieBackdrop.load("${BuildConfig.IMAGE_BASE_URL}${movieImage.backdrops.first().file_path}") {
                    error(R.drawable.ic_image)
                    placeholder(R.drawable.ic_image)
                    fallback(R.drawable.ic_image)
                }
            }
            if (movieImage.posters.isNotEmpty()) {
                ivMoviePoster.load("${BuildConfig.IMAGE_BASE_URL}${movieImage.posters.first().file_path}") {
                    error(R.drawable.ic_image)
                    placeholder(R.drawable.ic_image)
                    fallback(R.drawable.ic_image)
                }
            }
        }
    }

    private fun setDesc(movieDetail: MovieDetailResponse) {
        with(binding) {
            tvMovieTitle.text = movieDetail.title
            tvMovieStarCount.text = movieDetail.vote_average.toString()
            tvMovieDate.text = movieDetail.release_date
            tvMovieDesc.text = movieDetail.overview
        }
    }


    private fun setFragmentResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            Constant.DetailDataListener.DETAIL_SCREEN,
            this,
        ) { _, bundle ->
            viewModel.getMovieDetail(bundle.getInt(Constant.DetailDataListener.DETAIL_MOVIE_ID))
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}