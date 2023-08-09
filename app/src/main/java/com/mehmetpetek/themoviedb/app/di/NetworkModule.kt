package com.mehmetpetek.themoviedb.app.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mehmetpetek.themoviedb.BuildConfig
import com.mehmetpetek.themoviedb.data.remote.TheMovieDBService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideChuckInterceptor(application: Application) =
        ChuckerInterceptor.Builder(application).build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        chuckInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(chuckInterceptor)
            }
            addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${BuildConfig.API_TOKEN}"
                        )
                        .build()
                )
            })
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTheMovieDBService(retrofit: Retrofit): TheMovieDBService {
        return retrofit.create(TheMovieDBService::class.java)
    }
}