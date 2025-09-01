package com.example.library.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.library.UserPreferences
import com.example.library.data.preferences.DefaultSessionManager
import com.example.library.domain.SessionManager
import com.example.library.data.preferences.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context:Context
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = {context.dataStoreFile("user_prefs.pb")}
        )

    @Provides
    @Singleton
    fun provideLogInStateStore(
        @ApplicationContext context: Context
    ):DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir,"logInState.preferences_pb") }
        )

    @Provides
    @Singleton
    fun provideSessionManager(
        userDataStore:DataStore<UserPreferences>,
        logInStateStore:DataStore<Preferences>
    ): SessionManager = DefaultSessionManager(userDataStore, logInStateStore)

}