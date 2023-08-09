package com.mehmetpetek.themoviedb.app.di

import com.mehmetpetek.themoviedb.data.local.db.MovieDBDao
import com.mehmetpetek.themoviedb.data.remote.TheMovieDBDataSource
import com.mehmetpetek.themoviedb.data.remote.TheMovieDBService
import com.mehmetpetek.themoviedb.data.repository.TheMovieDBRepositoryImpl
import com.mehmetpetek.themoviedb.data.repository.TheMovieDBStorageRepositoryImpl
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieDBRepository(
        movieDBDao: MovieDBDao,
    ): TheMovieDBStorageRepository = TheMovieDBStorageRepositoryImpl(movieDBDao)

    @Provides
    @Singleton
    fun provideTheMovieDBRepository(
        theMovieDBDataSource: TheMovieDBDataSource,
        theMovieDBStorageRepository: TheMovieDBStorageRepository,
    ): TheMovieDBRepository =
        TheMovieDBRepositoryImpl(theMovieDBDataSource, theMovieDBStorageRepository)

    @Provides
    @Singleton
    fun provideTheMovieDBDataSource(
        theMovieDBService: TheMovieDBService,
    ): TheMovieDBDataSource = TheMovieDBDataSource(theMovieDBService)

}