package com.mehmetpetek.themoviedb.presentation.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.domain.usecase.AllMoviesUseCase
import com.mehmetpetek.themoviedb.presentation.base.BaseViewModel
import com.mehmetpetek.themoviedb.presentation.base.IEffect
import com.mehmetpetek.themoviedb.presentation.base.IEvent
import com.mehmetpetek.themoviedb.presentation.base.IState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val allMoviesUseCase: AllMoviesUseCase,
    private val application: Application
) : BaseViewModel<HomeEvent, HomeState, HomeEffect>() {

    init {
        allMovies(
            getCurrentState().popularMoviesPage,
            getCurrentState().topRatedMoviesPage,
            getCurrentState().upcomingMoviesPage,
            getCurrentState().nowPlayingMoviesPage
        )
    }

    override fun setInitialState(): HomeState = HomeState(isLoading = false)

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadMore -> {
                when (event.title) {
                    application.applicationContext.getString(R.string.popularity_movies) -> {
                        setState { copy(popularMoviesPage = getCurrentState().popularMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.revenue_movies) -> {
                        setState { copy(topRatedMoviesPage = getCurrentState().topRatedMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.primary_release_date_movies) -> {
                        setState { copy(upcomingMoviesPage = getCurrentState().upcomingMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.vote_average_movies) -> {
                        setState { copy(nowPlayingMoviesPage = getCurrentState().nowPlayingMoviesPage + 1) }
                    }
                }
                allMovies(
                    getCurrentState().popularMoviesPage,
                    getCurrentState().topRatedMoviesPage,
                    getCurrentState().upcomingMoviesPage,
                    getCurrentState().nowPlayingMoviesPage
                )
            }

            is HomeEvent.OnClickMovieDetail -> {
                setEffect { HomeEffect.GoToMovieDetail(event.movieId) }
            }
        }
    }

    private fun allMovies(
        popularMoviesPage: Int,
        topRatedMoviesPage: Int,
        upcomingMoviesPage: Int,
        nowPlayingMoviesPage: Int
    ) {
        viewModelScope.launch {
            allMoviesUseCase.invoke(
                popularMoviesPage,
                topRatedMoviesPage,
                upcomingMoviesPage,
                nowPlayingMoviesPage
            ).collect {
                when (it) {
                    is AllMoviesUseCase.AllMoviesState.Success -> {
                        setState { copy(allMovies = it.allMovies) }
                    }

                    is AllMoviesUseCase.AllMoviesState.NotData -> {
                    }

                    is AllMoviesUseCase.AllMoviesState.Error -> {

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
    val allMovies: HashMap<AllMoviesUseCase.MovieType, MovieResponse?> = hashMapOf(),
) : IState

sealed interface HomeEffect : IEffect {
    data class ShowError(val message: String) : HomeEffect
    data class GoToMovieDetail(val movieId: Int) : HomeEffect
}

sealed interface HomeEvent : IEvent {
    data class LoadMore(val title: String) : HomeEvent
    data class OnClickMovieDetail(val movieId: Int) : HomeEvent
}