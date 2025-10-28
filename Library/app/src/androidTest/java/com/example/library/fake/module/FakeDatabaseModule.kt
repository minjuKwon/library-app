package com.example.library.fake.module

import android.content.Context
import androidx.room.Room
import com.example.library.core.TimeProvider
import com.example.library.data.repository.CacheBookRepository
import com.example.library.data.room.BookCacheDao
import com.example.library.data.room.LibraryDatabase
import com.example.library.di.DatabaseModule
import com.example.library.domain.LocalRepository
import com.example.library.fake.FakeTimeProvider
import com.example.library.service.CacheBookService
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components= [SingletonComponent::class],
    replaces= [DatabaseModule::class]
)
object FakeDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):LibraryDatabase{
        return Room.inMemoryDatabaseBuilder(context,LibraryDatabase::class.java)
            .allowMainThreadQueries()
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
        return FakeTimeProvider()
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