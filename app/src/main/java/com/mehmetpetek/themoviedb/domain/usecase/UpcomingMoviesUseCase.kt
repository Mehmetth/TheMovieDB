package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UpcomingMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(page: Int): Flow<UpcomingMoviesState> = callbackFlow {
        theMovieDBRepository.getUpcomingMovies(page).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        trySend(
                            UpcomingMoviesState.Success(result.data)
                        )
                    } ?: kotlin.run {
                        UpcomingMoviesState.NotData
                    }
                }

                is Resource.Error,
                is Resource.Fail -> {
                    trySend(UpcomingMoviesState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed interface UpcomingMoviesState {
        data class Success(val upcomingMovies: MovieResponse) : UpcomingMoviesState
        data class Error(val throwable: Throwable?) : UpcomingMoviesState
        object NotData : UpcomingMoviesState
    }
}