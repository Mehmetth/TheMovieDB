package com.mehmetpetek.themoviedb.domain.repository

import com.mehmetpetek.themoviedb.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface TheMovieDBStorageRepository {
    suspend fun insertMovies(movies: List<MovieEntity>)
    fun getMovies(page: Int, sortBy: String): Flow<List<MovieEntity>>
}