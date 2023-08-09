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
            getCurrentState().popularityMoviesPage,
            getCurrentState().revenueMoviesPage,
            getCurrentState().primaryReleaseDateMoviesPage,
            getCurrentState().voteAverageMoviesPage
        )
    }

    override fun setInitialState(): HomeState = HomeState(isLoading = false)

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadMore -> {
                when (event.title) {
                    application.applicationContext.getString(R.string.popularity_movies) -> {
                        setState { copy(popularityMoviesPage = getCurrentState().popularityMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.revenue_movies) -> {
                        setState { copy(revenueMoviesPage = getCurrentState().revenueMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.primary_release_date_movies) -> {
                        setState { copy(primaryReleaseDateMoviesPage = getCurrentState().primaryReleaseDateMoviesPage + 1) }
                    }

                    application.applicationContext.getString(R.string.vote_average_movies) -> {
                        setState { copy(voteAverageMoviesPage = getCurrentState().voteAverageMoviesPage + 1) }
                    }
                }
                allMovies(
                    getCurrentState().popularityMoviesPage,
                    getCurrentState().revenueMoviesPage,
                    getCurrentState().primaryReleaseDateMoviesPage,
                    getCurrentState().voteAverageMoviesPage
                )
            }

            is HomeEvent.OnClickMovieDetail -> {
                setEffect { HomeEffect.GoToMovieDetail(event.movieId) }
            }
        }
    }

    private fun allMovies(
        popularityMoviesPage: Int,
        revenueMoviesPage: Int,
        primaryReleaseDateMoviesPage: Int,
        voteAverageMoviesPage: Int
    ) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            allMoviesUseCase.invoke(
                popularityMoviesPage,
                revenueMoviesPage,
                primaryReleaseDateMoviesPage,
                voteAverageMoviesPage
            ).collect {
                when (it) {
                    is AllMoviesUseCase.AllMoviesState.Success -> {
                        setState { copy(isLoading = false, allMovies = it.allMovies) }
                    }

                    is AllMoviesUseCase.AllMoviesState.NotData -> {
                        setState { copy(isLoading = false, allMovies = hashMapOf()) }
                    }

                    is AllMoviesUseCase.AllMoviesState.Error -> {
                        setState { copy(isLoading = false, allMovies = hashMapOf()) }
                        setEffect { HomeEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }
                }
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val popularityMoviesPage: Int = 1,
    val revenueMoviesPage: Int = 1,
    val primaryReleaseDateMoviesPage: Int = 1,
    val voteAverageMoviesPage: Int = 1,
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