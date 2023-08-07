package com.mehmetpetek.themoviedb.presentation.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Result
import com.mehmetpetek.themoviedb.domain.usecase.NowPlayingMoviesUseCase
import com.mehmetpetek.themoviedb.domain.usecase.PopularMoviesUseCase
import com.mehmetpetek.themoviedb.domain.usecase.TopRatedMoviesUseCase
import com.mehmetpetek.themoviedb.domain.usecase.UpcomingMoviesUseCase
import com.mehmetpetek.themoviedb.presentation.base.BaseViewModel
import com.mehmetpetek.themoviedb.presentation.base.IEffect
import com.mehmetpetek.themoviedb.presentation.base.IEvent
import com.mehmetpetek.themoviedb.presentation.base.IState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private var popularMoviesUseCase: PopularMoviesUseCase,
    private var topRatedMoviesUseCase: TopRatedMoviesUseCase,
    private var upcomingMoviesUseCase: UpcomingMoviesUseCase,
    private var nowPlayingMoviesUseCase: NowPlayingMoviesUseCase,
    private val application: Application
) : BaseViewModel<HomeEvent, HomeState, HomeEffect>() {

    init {
        getPopularMovies(getCurrentState().popularMoviesPage)
        topRatedMovies(getCurrentState().topRatedMoviesPage)
        getUpcomingMovies(getCurrentState().upcomingMoviesPage)
        getNowPlayingMovies(getCurrentState().nowPlayingMoviesPage)
    }

    override fun setInitialState(): HomeState = HomeState(isLoading = false)

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadMore -> {
                when (event.title) {
                    application.applicationContext.getString(R.string.popular_movies) -> {
                        getPopularMovies(getCurrentState().popularMoviesPage + 1)
                    }

                    application.applicationContext.getString(R.string.top_rated_movies) -> {
                        topRatedMovies(getCurrentState().topRatedMoviesPage + 1)
                    }

                    application.applicationContext.getString(R.string.up_coming_movies) -> {
                        getUpcomingMovies(getCurrentState().upcomingMoviesPage + 1)
                    }

                    application.applicationContext.getString(R.string.now_playing_movies) -> {
                        getNowPlayingMovies(getCurrentState().nowPlayingMoviesPage + 1)
                    }
                }
            }

            is HomeEvent.OnClickMovieDetail -> {
                setEffect { HomeEffect.GoToMovieDetail(event.movieId) }
            }
        }
    }

    private fun getPopularMovies(page: Int) {
        setState { copy(isLoading = true, popularMoviesPage = page) }

        viewModelScope.launch {
            popularMoviesUseCase.invoke(page).collect {
                when (it) {
                    is PopularMoviesUseCase.PopularMoviesState.Success -> {
                        setState { HomeState(popularMovies = it.popularMovies) }
                    }

                    is PopularMoviesUseCase.PopularMoviesState.NotData -> {
                        setState { copy(isLoading = false, popularMovies = null) }
                    }

                    is PopularMoviesUseCase.PopularMoviesState.Error -> {
                        setState { copy(isLoading = false, popularMovies = null) }
                        setEffect { HomeEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }
                }
            }
        }
    }

    private fun topRatedMovies(page: Int) {
        setState { copy(isLoading = true, topRatedMoviesPage = page) }
        setState { copy() }
        viewModelScope.launch {
            topRatedMoviesUseCase.invoke(page).collect {
                when (it) {
                    is TopRatedMoviesUseCase.TopRatedMoviesState.Success -> {
                        setState { HomeState(topRatedMovies = it.topRatedMovies) }
                    }

                    is TopRatedMoviesUseCase.TopRatedMoviesState.NotData -> {
                        setState { copy(isLoading = false, topRatedMovies = null) }
                    }

                    is TopRatedMoviesUseCase.TopRatedMoviesState.Error -> {
                        setState { copy(isLoading = false, topRatedMovies = null) }
                        setEffect { HomeEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovies(page: Int) {
        setState { copy(isLoading = true, upcomingMoviesPage = page) }
        viewModelScope.launch {
            upcomingMoviesUseCase.invoke(page).collect {
                when (it) {
                    is UpcomingMoviesUseCase.UpcomingMoviesState.Success -> {
                        setState { HomeState(upcomingMovies = it.upcomingMovies) }
                    }

                    is UpcomingMoviesUseCase.UpcomingMoviesState.NotData -> {
                        setState { copy(isLoading = false, upcomingMovies = null) }
                    }

                    is UpcomingMoviesUseCase.UpcomingMoviesState.Error -> {
                        setState { copy(isLoading = false, upcomingMovies = null) }
                        setEffect { HomeEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }
                }
            }
        }
    }

    private fun getNowPlayingMovies(page: Int) {
        setState { copy(isLoading = true, nowPlayingMoviesPage = page) }
        viewModelScope.launch {
            nowPlayingMoviesUseCase.invoke(page).collect {
                when (it) {
                    is NowPlayingMoviesUseCase.NowPlayingMoviesState.Success -> {
                        setState { HomeState(nowPlayingMovies = it.nowPlayingMovies) }
                    }

                    is NowPlayingMoviesUseCase.NowPlayingMoviesState.NotData -> {
                        setState { copy(isLoading = false, nowPlayingMovies = null) }
                    }

                    is NowPlayingMoviesUseCase.NowPlayingMoviesState.Error -> {
                        setState { copy(isLoading = false, nowPlayingMovies = null) }
                        setEffect { HomeEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }
                }
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val popularMoviesPage: Int = 1,
    val topRatedMoviesPage: Int = 1,
    val upcomingMoviesPage: Int = 1,
    val nowPlayingMoviesPage: Int = 1,
    val popularMovies: MovieResponse? = null,
    val topRatedMovies: MovieResponse? = null,
    val upcomingMovies: MovieResponse? = null,
    val nowPlayingMovies: MovieResponse? = null
) : IState

sealed interface HomeEffect : IEffect {
    data class ShowError(val message: String) : HomeEffect
    data class GoToMovieDetail(val movieId: Int) : HomeEffect
}

sealed interface HomeEvent : IEvent {
    data class LoadMore(val title: String) : HomeEvent
    data class OnClickMovieDetail(val movieId: Int) : HomeEvent
}