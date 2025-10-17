package com.example.library.di

import com.example.library.domain.LibrarySyncService
import com.example.library.domain.RemoteRepository
import com.example.library.service.CacheBookService
import com.example.library.service.DefaultLibrarySyncService
import com.example.library.service.FirebaseBookService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibrarySyncServiceModule {

    @Provides
    @Singleton
    fun provideLibrarySyncService(
        remoteRepository: RemoteRepository,
        cacheBookService: CacheBookService,
        firebaseBookService: FirebaseBookService
    ): LibrarySyncService{
        return DefaultLibrarySyncService(remoteRepository,cacheBookService, firebaseBookService)
    }

}