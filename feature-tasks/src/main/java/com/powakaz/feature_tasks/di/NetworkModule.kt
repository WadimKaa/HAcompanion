package com.powakaz.feature_tasks.di

import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideNetworkTodoListApi(retrofit: Retrofit) : NetworkTodoListApi{
        return retrofit.create(NetworkTodoListApi::class.java)
    }
}