package com.example.library.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.library.UserPreferences
import com.example.library.data.entity.Gender
import com.example.library.data.entity.User
import com.example.library.domain.SessionManager
import com.example.library.Gender as ProtoGender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSessionManager @Inject constructor(
    private val userDataStore:DataStore<UserPreferences>,
    private val logInStateStore:DataStore<Preferences>
): SessionManager {

    private companion object{
        val IS_LOG_IN= booleanPreferencesKey("isLogIn")
    }

    override val userPreferences:Flow<User> = userDataStore.data.map { it.toUser() }
    override val logInPreferences:Flow<Boolean> = logInStateStore.data.map { it[IS_LOG_IN] ?: false }

    override suspend fun saveUserData(user: User){
        userDataStore.updateData { user.toProto() }
    }

    override suspend fun removeUserData(){
        userDataStore.updateData { UserPreferences.getDefaultInstance() }
    }

    override suspend fun saveLogInState(isLogIn:Boolean){
        logInStateStore.edit { it[IS_LOG_IN]= isLogIn }
    }

    override suspend fun removeLogInState(){
        logInStateStore.edit { it.clear() }
    }

    private fun UserPreferences.toUser()= User(uid,email, name, gender.toUser(), age)

    private fun User.toProto(): UserPreferences =
        UserPreferences.newBuilder()
            .setUid(uid)
            .setName(name)
            .setEmail(email)
            .setAge(age)
            .setGender(gender.toProto())
            .build()

    private fun ProtoGender.toUser(): Gender = when(this){
        ProtoGender.MALE -> Gender.MALE
        ProtoGender.FEMALE -> Gender.FEMALE
        else -> throw UnsupportedOperationException("$this not supported")
    }

    private fun Gender.toProto(): ProtoGender = when(this){
        Gender.MALE -> ProtoGender.MALE
        Gender.FEMALE -> ProtoGender.FEMALE
    }

}