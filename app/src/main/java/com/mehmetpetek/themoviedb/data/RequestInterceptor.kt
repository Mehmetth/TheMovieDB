package com.mehmetpetek.themoviedb.data

import com.mehmetpetek.themoviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer ${BuildConfig.API_TOKEN}"
                )
                .build()
        )
    }
}