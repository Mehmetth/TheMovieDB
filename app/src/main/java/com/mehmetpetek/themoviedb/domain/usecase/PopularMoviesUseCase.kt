package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PopularMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(page: Int): Flow<PopularMoviesState> = callbackFlow {
        theMovieDBRepository.getPopularMovies(page).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        trySend(
                            PopularMoviesState.Success(result.data)
                        )
                    } ?: kotlin.run {
                        PopularMoviesState.NotData
                    }
                }

                is Resource.Error,
                is Resource.Fail -> {
                    trySend(PopularMoviesState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed interface PopularMoviesState {
        data class Success(val popularMovies: MovieResponse) : PopularMoviesState
        data class Error(val throwable: Throwable?) : PopularMoviesState
        object NotData : PopularMoviesState
    }
}