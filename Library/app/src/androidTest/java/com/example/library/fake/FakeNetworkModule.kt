package com.example.library.fake

import com.example.library.di.NetworkModule
import com.example.library.domain.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

import javax.inject.Singleton

@Module
@TestInstallIn(
    components= [SingletonComponent::class],
    replaces= [NetworkModule::class]
)
object FakeNetworkModule {
    @Provides
    @Singleton
    fun provideNetworkBookRepository(): RemoteRepository {
        return FakeNetworkBookRepository()
    }
}