package com.powakaz.core_network.factory

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val token : String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()


        return chain.proceed(modifiedRequest)
    }
}