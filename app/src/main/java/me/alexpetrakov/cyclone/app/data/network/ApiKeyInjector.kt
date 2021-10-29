package me.alexpetrakov.cyclone.app.data.network

import me.alexpetrakov.cyclone.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInjector : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (originalRequest.url().queryParameter(PARAMETER_APP_ID) != null) {
            return chain.proceed(originalRequest)
        }
        val updatedUrl = originalRequest.url().newBuilder()
            .addQueryParameter(PARAMETER_APP_ID, BuildConfig.API_KEY)
            .build()
        val updatedRequest = originalRequest.newBuilder()
            .url(updatedUrl)
            .build()
        return chain.proceed(updatedRequest)
    }

    companion object {
        private const val PARAMETER_APP_ID = "appid"
    }
}