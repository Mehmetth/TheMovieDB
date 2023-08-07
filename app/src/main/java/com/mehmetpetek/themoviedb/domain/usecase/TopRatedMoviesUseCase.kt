package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TopRatedMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(page: Int): Flow<TopRatedMoviesState> = callbackFlow {
        theMovieDBRepository.getTopRatedMovies(page).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        trySend(
                            TopRatedMoviesState.Success(result.data)
                        )
                    } ?: kotlin.run {
                        TopRatedMoviesState.NotData
                    }
                }

                is Resource.Error,
                is Resource.Fail -> {
                    trySend(TopRatedMoviesState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed interface TopRatedMoviesState {
        data class Success(val topRatedMovies: MovieResponse) : TopRatedMoviesState
        data class Error(val throwable: Throwable?) : TopRatedMoviesState
        object NotData : TopRatedMoviesState
    }
}