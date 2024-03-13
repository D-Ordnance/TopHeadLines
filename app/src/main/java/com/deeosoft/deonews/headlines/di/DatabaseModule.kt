package com.deeosoft.deonews.headlines.di

import android.content.Context
import androidx.room.Room
import com.deeosoft.deonews.headlines.db.HeadLineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataBaseObject(context: Context): HeadLineDatabase {
        return Room.databaseBuilder(
            context,
            HeadLineDatabase::class.java,
            "top_head_lines"
        ).build()
    }
}