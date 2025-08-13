package com.example.library.di

import com.example.library.BuildConfig
import com.example.library.data.SessionManager
import com.example.library.data.repository.FirebaseUserRepository
import com.example.library.domain.UserRepository
import com.example.library.domain.UserService
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
        val auth= FirebaseAuth.getInstance()
        if (BuildConfig.DEBUG) auth.useEmulator("127.0.0.1", 9099)
        return auth
    }

    @Provides
    @Singleton
    fun provideFirebaseStore():FirebaseFirestore{
        val db = FirebaseFirestore.getInstance()
        if (BuildConfig.DEBUG) db.useEmulator("127.0.0.1", 8080)
        return db
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
    ): UserService {
        return FirebaseUserService(userRepository, defaultSessionManager)
    }

}