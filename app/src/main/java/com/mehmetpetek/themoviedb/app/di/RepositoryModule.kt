package com.mehmetpetek.themoviedb.app.di

import com.mehmetpetek.themoviedb.data.remote.TheMovieDBDataSource
import com.mehmetpetek.themoviedb.data.remote.TheMovieDBService
import com.mehmetpetek.themoviedb.data.repository.TheMovieDBRepositoryImpl
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
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
    fun provideTheMovieDBRepository(
        theMovieDBDataSource: TheMovieDBDataSource,
    ): TheMovieDBRepository = TheMovieDBRepositoryImpl(theMovieDBDataSource)

    @Provides
    @Singleton
    fun provideTheMovieDBDataSource(
        theMovieDBService: TheMovieDBService,
    ): TheMovieDBDataSource = TheMovieDBDataSource(theMovieDBService)
}