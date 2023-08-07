package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NowPlayingMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(page: Int): Flow<NowPlayingMoviesState> = callbackFlow {
        theMovieDBRepository.getNowPlayingMovies(page).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        trySend(
                            NowPlayingMoviesState.Success(result.data)
                        )
                    } ?: kotlin.run {
                        NowPlayingMoviesState.NotData
                    }
                }

                is Resource.Error,
                is Resource.Fail -> {
                    trySend(NowPlayingMoviesState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed interface NowPlayingMoviesState {
        data class Success(val nowPlayingMovies: MovieResponse) : NowPlayingMoviesState
        data class Error(val throwable: Throwable?) : NowPlayingMoviesState
        object NotData : NowPlayingMoviesState
    }
}