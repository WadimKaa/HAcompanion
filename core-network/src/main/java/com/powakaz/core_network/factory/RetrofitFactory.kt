package com.powakaz.core_network.factory

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitFactory {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun createRetrofit(baseUrl : String, okHttpClient: OkHttpClient) : Retrofit{
        val contentType = "application/json".toMediaType()


        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    inline fun <reified T> createApi(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }
}