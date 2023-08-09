package com.mehmetpetek.themoviedb.presentation.detail

import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
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
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.DRM_LICENSE_URL
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.DRM_SCHEME
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.URL
import com.mehmetpetek.themoviedb.common.Constant.DrmMovie.USER_AGENT
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
        binding.ivPlayBtn.setOnClickListener {
            it.visibility = View.GONE
            binding.epPlayer.visibility = View.VISIBLE
            binding.clMovieAttr.visibility = View.GONE
            binding.epPlayer.player?.playWhenReady = true
            binding.ivCloseBtn.visibility = View.VISIBLE
            binding.ivFullScreenBtn.visibility = View.VISIBLE
            binding.ivFullScreenBtn.load(R.drawable.ic_fullscreen)
        }

        binding.ivCloseBtn.setOnClickListener {
            releasePlayer()
            it.visibility = View.GONE
            binding.ivFullScreenBtn.visibility = View.GONE
            binding.epPlayer.visibility = View.GONE
            binding.clMovieAttr.visibility = View.VISIBLE
            binding.ivPlayBtn.visibility = View.VISIBLE
            initializePlayer()
        }

        binding.ivFullScreenBtn.setOnClickListener {
            if (!fullScreenVideo) {
                binding.ivFullScreenBtn.load(R.drawable.ic_fullscreen_exit)
                fullScreen()
            } else {
                binding.ivFullScreenBtn.load(R.drawable.ic_fullscreen)
                exitFullScreen()
            }
            fullScreenVideo = !fullScreenVideo
        }
    }

    private fun fullScreen() {
        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        if (requireActivity().actionBar != null) {
            requireActivity().actionBar?.hide()
        }
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val params = binding.clTopArea.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.clTopArea.layoutParams = params
    }

    private fun exitFullScreen() {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        if (requireActivity().actionBar != null) {
            requireActivity().actionBar?.show()
        }

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val params = binding.clTopArea.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height =
            (350 * requireContext().resources.displayMetrics.density).toInt()
        binding.clTopArea.layoutParams = params
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