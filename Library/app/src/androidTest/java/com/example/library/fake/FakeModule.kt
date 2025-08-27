package com.example.library.fake

import dagger.Module
import dagger.Provides
import com.example.library.data.SessionManager
import com.example.library.di.FirebaseModule
import com.example.library.domain.UserRepository
import com.example.library.domain.UserService
import com.example.library.service.FirebaseUserService
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirebaseModule::class]
)
object FakeModule {

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

}