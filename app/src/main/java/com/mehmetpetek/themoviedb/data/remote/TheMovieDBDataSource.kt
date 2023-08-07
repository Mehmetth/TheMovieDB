package com.mehmetpetek.themoviedb.data.remote

import javax.inject.Inject

class TheMovieDBDataSource @Inject constructor(private val theMovieDBService: TheMovieDBService) {

    suspend fun getPopularMovies(page: Int) = theMovieDBService.getPopularMovies(page)
    suspend fun getTopRatedMovies(page: Int) = theMovieDBService.getTopRatedMovies(page)
    suspend fun getUpcomingMovies(page: Int) = theMovieDBService.getUpcomingMovies(page)
    suspend fun getNowPlayingMovies(page: Int) = theMovieDBService.getNowPlayingMovies(page)
}