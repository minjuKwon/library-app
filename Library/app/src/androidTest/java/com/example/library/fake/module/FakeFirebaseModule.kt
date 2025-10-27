package com.example.library.fake.module

import dagger.Module
import dagger.Provides
import com.example.library.domain.SessionManager
import com.example.library.di.FirebaseModule
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.DatabaseService
import com.example.library.domain.UserRepository
import com.example.library.domain.UserService
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.FakeUserRepository
import com.example.library.service.FirebaseBookService
import com.example.library.service.FirebaseUserService
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirebaseModule::class]
)
object FakeFirebaseModule {

    @Provides
    @Singleton
    fun provideFakeUserRepository(): UserRepository{
        return FakeUserRepository()
    }

    @Provides
    @Singleton
    fun provideFirebaseUserService(
        userRepository: UserRepository,
        defaultSessionManager: SessionManager
    ): UserService {
        return FirebaseUserService(userRepository, defaultSessionManager)
    }

    @Provides
    @Singleton
    fun provideFirebaseBookRepository(): DatabaseRepository {
        return FakeBookRepository()
    }

    @Provides
    @Singleton
    fun provideFirebaseBookService(
        databaseRepository: DatabaseRepository,
        defaultSessionManager: SessionManager
    ): DatabaseService {
        return FirebaseBookService(databaseRepository, defaultSessionManager)
    }

}