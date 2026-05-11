package com.powakaz.feature_tasks.di

import com.powakaz.feature_tasks.data.repository.TodoRepositoryImpl
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl) : TodoRepository
}