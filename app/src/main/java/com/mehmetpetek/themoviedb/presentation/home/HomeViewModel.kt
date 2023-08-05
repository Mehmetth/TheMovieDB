package com.mehmetpetek.themoviedb.presentation.home

import com.mehmetpetek.themoviedb.presentation.base.BaseViewModel
import com.mehmetpetek.themoviedb.presentation.base.IEffect
import com.mehmetpetek.themoviedb.presentation.base.IEvent
import com.mehmetpetek.themoviedb.presentation.base.IState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeEvent, HomeState, HomeEffect>() {

    override fun setInitialState(): HomeState = HomeState(isLoading = false)

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            else -> {}
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false
) : IState

sealed interface HomeEffect : IEffect {
    data class ShowError(val message: String) : HomeEffect
}

sealed interface HomeEvent : IEvent