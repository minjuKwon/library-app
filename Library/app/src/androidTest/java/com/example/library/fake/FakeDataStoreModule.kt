package com.example.library.fake

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.library.UserPreferences
import com.example.library.data.DefaultSessionManager
import com.example.library.data.SessionManager
import com.example.library.data.UserPreferencesSerializer
import com.example.library.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object FakeDataStoreModule {

    //DataStore 인스턴스 캐시 문제로 별도 생성
    private var cnt=0

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context:Context
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = {context.dataStoreFile("user_prefs_test${cnt++}.pb")}
        )

    @Provides
    @Singleton
    fun provideLogInStateStore(
        @ApplicationContext context: Context
    ):DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir,"logInState_test${cnt++}.preferences_pb") }
        )

    @Provides
    @Singleton
    fun provideSessionManager(
        userDataStore:DataStore<UserPreferences>,
        logInStateStore:DataStore<Preferences>
    ):SessionManager = DefaultSessionManager(userDataStore, logInStateStore)

}