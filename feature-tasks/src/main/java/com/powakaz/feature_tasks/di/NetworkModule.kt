package com.powakaz.feature_tasks.di

import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.powakaz.feature_tasks.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideApi() : NetworkTodoListApi{
        val authInterceptor = AuthInterceptor(BuildConfig.AUTH_TOKEN)
        val okhttpClient = NetworkFactory.createOkHttpClient(authInterceptor)
        val retrofitClient = RetrofitFactory.createRetrofit(BuildConfig.BASE_URL, okhttpClient)


        return RetrofitFactory.createApi(retrofitClient)
    }
}