package com.mehmetpetek.themoviedb.presentation.player

import com.mehmetpetek.themoviedb.presentation.base.BaseViewModel
import com.mehmetpetek.themoviedb.presentation.base.IEffect
import com.mehmetpetek.themoviedb.presentation.base.IEvent
import com.mehmetpetek.themoviedb.presentation.base.IState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
) : BaseViewModel<PlayerEvent, PlayerState, PlayerEffect>() {

    override fun setInitialState(): PlayerState = PlayerState(isLoading = false)

    override fun handleEvents(event: PlayerEvent) {
        when (event) {
            else -> {}
        }
    }
}

data class PlayerState(
    val isLoading: Boolean = false
) : IState

sealed interface PlayerEffect : IEffect {
    data class ShowError(val message: String) : PlayerEffect
}

sealed interface PlayerEvent : IEvent