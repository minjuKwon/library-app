package com.example.library.fake.module

import com.example.library.di.NetworkModule
import com.example.library.domain.RemoteRepository
import com.example.library.fake.repository.FakeNetworkBookRepository
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