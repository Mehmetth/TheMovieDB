package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class AllMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(
        popularityMoviesPage: Int,
        revenueMoviesPage: Int,
        primaryReleaseDateMoviesPage: Int,
        voteAverageMoviesPage: Int
    ): Flow<AllMoviesState> = callbackFlow {

        val hashMap: HashMap<MovieType, MovieResponse?> = hashMapOf()

        val getPopularityMovies = theMovieDBRepository.getDiscoverMovies(
            popularityMoviesPage,
            MovieType.POPULARITY_DESC.movieFilterDesc
        )
        val getRevenueMovies = theMovieDBRepository.getDiscoverMovies(
            revenueMoviesPage,
            MovieType.REVENUE_DESC.movieFilterDesc
        )
        val getPrimaryReleaseDateMovies = theMovieDBRepository.getDiscoverMovies(
            primaryReleaseDateMoviesPage,
            MovieType.PRIMARY_RELEASE_DATE_DESC.movieFilterDesc
        )
        val getVoteAverageMovies = theMovieDBRepository.getDiscoverMovies(
            voteAverageMoviesPage,
            MovieType.VOTE_AVERAGE_DESC.movieFilterDesc
        )

        combine(
            getPopularityMovies,
            getRevenueMovies,
            getPrimaryReleaseDateMovies,
            getVoteAverageMovies
        ) { popularityMovies, revenueMovies, primaryReleaseDateMovies, voteAverageMovies ->

            popularityMovies.data?.let {
                if (it.results.isNotEmpty()) {
                    hashMap[MovieType.POPULARITY_DESC] = popularityMovies.data
                } else {
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(popularityMovies.message)
            }

            revenueMovies.data?.let {
                if (it.results.isNotEmpty()) {
                    hashMap[MovieType.REVENUE_DESC] = revenueMovies.data
                } else {
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(revenueMovies.message)
            }

            primaryReleaseDateMovies.data?.let {
                if (it.results.isNotEmpty()) {
                    hashMap[MovieType.PRIMARY_RELEASE_DATE_DESC] = primaryReleaseDateMovies.data
                } else {
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(primaryReleaseDateMovies.message)
            }

            voteAverageMovies.data?.let {
                if (it.results.isNotEmpty()) {
                    hashMap[MovieType.VOTE_AVERAGE_DESC] = voteAverageMovies.data
                } else {
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(voteAverageMovies.message)
            }

            if (hashMap.values.isNotEmpty()) {
                AllMoviesState.Success(hashMap)
            } else {
                AllMoviesState.Error(Throwable())
            }

        }.collect {
            trySend(it)
        }
        awaitClose { cancel() }
    }

    sealed interface AllMoviesState {
        data class Success(val allMovies: HashMap<MovieType, MovieResponse?>) : AllMoviesState
        data class Error(val throwable: Throwable?) : AllMoviesState
        object NotData : AllMoviesState
    }


    enum class MovieType(val movieType: Int, val movieFilterDesc: String) {
        POPULARITY_DESC(R.string.popularity_movies, "popularity.desc"),
        REVENUE_DESC(R.string.revenue_movies, "revenue.desc"),
        PRIMARY_RELEASE_DATE_DESC(
            R.string.primary_release_date_movies,
            "primary_release_date.desc"
        ),
        VOTE_AVERAGE_DESC(R.string.vote_average_movies, "vote_average.desc")
    }
}