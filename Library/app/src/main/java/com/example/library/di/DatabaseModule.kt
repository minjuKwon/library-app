package com.example.library.di

import android.content.Context
import androidx.room.Room
import com.example.library.data.room.BookCacheDao
import com.example.library.data.room.LibraryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):LibraryDatabase{
        return Room.databaseBuilder(
            context,LibraryDatabase::class.java,"library_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideBookDao(database: LibraryDatabase):BookCacheDao{
        return database.bookCacheDao()
    }

}