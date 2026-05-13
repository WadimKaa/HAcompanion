package com.powakaz.core_network.di

import com.powakaz.core_network.BuildConfig
import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(BuildConfig.HA_TOKEN)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return NetworkFactory.createOkHttpClient(authInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitFactory.createRetrofit(BuildConfig.HA_BASE_URL, okHttpClient)
    }


}