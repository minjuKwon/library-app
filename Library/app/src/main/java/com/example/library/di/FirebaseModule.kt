package com.example.library.di

import com.example.library.data.SessionManager
import com.example.library.data.repository.FirebaseUserRepository
import com.example.library.domain.UserRepository
import com.example.library.service.FirebaseUserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseUserRepository(
        firebaseAuth: FirebaseAuth,
        fireStore:FirebaseFirestore
    ): UserRepository {
        return FirebaseUserRepository(firebaseAuth, fireStore)
    }


    @Provides
    @Singleton
    fun provideFirebaseUserService(
        userRepository: UserRepository,
        defaultSessionManager: SessionManager
    ): FirebaseUserService {
        return FirebaseUserService(userRepository, defaultSessionManager)
    }

}