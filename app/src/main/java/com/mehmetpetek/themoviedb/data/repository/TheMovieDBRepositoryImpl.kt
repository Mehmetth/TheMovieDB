package com.mehmetpetek.themoviedb.data.repository

import com.mehmetpetek.themoviedb.data.remote.TheMovieDBDataSource
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TheMovieDBRepositoryImpl @Inject constructor(private val theMovieDBDataSource: TheMovieDBDataSource) :
    TheMovieDBRepository {
    override fun getPopularMovies(page: Int): Flow<Resource<MovieResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getPopularMovies(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

    override fun getTopRatedMovies(page: Int): Flow<Resource<MovieResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getTopRatedMovies(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

    override fun getUpcomingMovies(page: Int): Flow<Resource<MovieResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getUpcomingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

    override fun getNowPlayingMovies(page: Int): Flow<Resource<MovieResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getNowPlayingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

}