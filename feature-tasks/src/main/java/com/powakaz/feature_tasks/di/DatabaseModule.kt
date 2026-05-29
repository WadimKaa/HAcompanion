package com.powakaz.feature_tasks.di

import android.content.Context
import androidx.room.Room
import com.powakaz.feature_tasks.data.local.TodoDatabase
import com.powakaz.feature_tasks.data.local.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_items.db"
        ).build()
    }


    @Provides
    fun providesTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }
}