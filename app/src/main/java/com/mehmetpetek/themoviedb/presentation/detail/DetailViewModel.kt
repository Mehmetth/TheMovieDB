package com.mehmetpetek.themoviedb.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mehmetpetek.themoviedb.common.Constant
import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.domain.usecase.MovieDetailUseCase
import com.mehmetpetek.themoviedb.presentation.base.BaseViewModel
import com.mehmetpetek.themoviedb.presentation.base.IEffect
import com.mehmetpetek.themoviedb.presentation.base.IEvent
import com.mehmetpetek.themoviedb.presentation.base.IState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private var movieDetailUseCase: MovieDetailUseCase
) : BaseViewModel<DetailEvent, DetailState, DetailEffect>() {

    init {
        savedStateHandle.get<Int>(Constant.MOVIE_ID)?.let {
            getMovieDetail(it)
        }
    }

    override fun setInitialState(): DetailState = DetailState(isLoading = false)

    override fun handleEvents(event: DetailEvent) {
        when (event) {
            else -> {}
        }
    }

    private fun getMovieDetail(movieId: Int) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            movieDetailUseCase.invoke(movieId).collect {
                when (it) {
                    is MovieDetailUseCase.MovieDetailState.Success -> {
                        setState {
                            copy(
                                isLoading = false,
                                movieDetailResponse = it.movieDetailResponse
                            )
                        }
                    }

                    is MovieDetailUseCase.MovieDetailState.Error -> {
                        setState { copy(isLoading = false) }
                        setEffect { DetailEffect.ShowError(it.throwable?.message.orEmpty()) }
                    }

                    MovieDetailUseCase.MovieDetailState.NotData -> {
                        setState { copy(isLoading = false, movieDetailResponse = null) }
                    }
                }
            }
        }
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val movieDetailResponse: MovieDetailResponse? = null
) : IState

sealed interface DetailEffect : IEffect {
    data class ShowError(val message: String) : DetailEffect
}

sealed interface DetailEvent : IEvent