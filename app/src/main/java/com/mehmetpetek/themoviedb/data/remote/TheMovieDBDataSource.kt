package com.mehmetpetek.themoviedb.data.remote

import javax.inject.Inject

class TheMovieDBDataSource @Inject constructor(private val theMovieDBService: TheMovieDBService) {

    suspend fun getDiscoverMovies(page: Int, sortBy: String) =
        theMovieDBService.getDiscoverMovies(page, sortBy)

    suspend fun getMovieDetail(movieId: Int) = theMovieDBService.getMovieDetail(movieId)
}