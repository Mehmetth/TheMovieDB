package com.mehmetpetek.themoviedb.data.repository

import com.mehmetpetek.themoviedb.data.local.db.MovieDBDao
import com.mehmetpetek.themoviedb.data.local.model.MovieEntity
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBStorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TheMovieDBStorageRepositoryImpl @Inject constructor(
    private val movieDBDao: MovieDBDao,
) : TheMovieDBStorageRepository {

    override suspend fun insertMovies(movies: List<MovieEntity>) {
        movieDBDao.insertMovies(movies)
    }

    override fun getMovies(page: Int, sortBy: String): Flow<List<MovieEntity>> {
        return movieDBDao.getMovies(page, sortBy)
    }
}