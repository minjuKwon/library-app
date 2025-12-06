package com.example.library.di

import android.content.Context
import androidx.room.Room
import com.example.library.core.DefaultTimeProvider
import com.example.library.core.TimeProvider
import com.example.library.data.repository.CacheBookRepository
import com.example.library.data.room.BookCacheDao
import com.example.library.data.room.LibraryDatabase
import com.example.library.domain.LocalRepository
import com.example.library.service.CacheBookService
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
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBookDao(database: LibraryDatabase):BookCacheDao{
        return database.bookCacheDao()
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        bookCacheDao: BookCacheDao
    ): LocalRepository {
        return CacheBookRepository(bookCacheDao)
    }

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider {
        return DefaultTimeProvider()
    }

    @Singleton
    @Provides
    fun provideCacheBookService(
        cacheBookRepository: LocalRepository,
        timeProvider: TimeProvider
    ): CacheBookService {
        return CacheBookService(cacheBookRepository, timeProvider)
    }

}