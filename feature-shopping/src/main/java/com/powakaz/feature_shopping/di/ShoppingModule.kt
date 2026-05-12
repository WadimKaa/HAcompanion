package com.powakaz.feature_shopping.di

import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.repository.ShoppingRepositoryImpl
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ShoppingModule {

    @Provides
    @Singleton
    fun provideShoppingApi(retrofit: Retrofit): ShoppingApi {
        return retrofit.create(ShoppingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(api: ShoppingApi): ShoppingRepository {
        return ShoppingRepositoryImpl(api)
    }
}