package com.powakaz.feature_shopping.di

import com.powakaz.feature_shopping.data.repository.ShoppingRepositoryImpl
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
abstract class ShoppingRepositoryModule {

    //@Binds
    //@Singleton
    //abstract fun bindShoppingRepository(shoppingRepositoryImpl: ShoppingRepositoryImpl): ShoppingRepository
}