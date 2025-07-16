package com.example.library.data

import androidx.datastore.core.DataStore
import com.example.library.UserPreferences
import com.example.library.Gender as ProtoGender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore:DataStore<UserPreferences>
){

    val userPreferences:Flow<User> = dataStore.data.map { it.toUser() }

    suspend fun saveSession(user:User){
        dataStore.updateData { user.toProto() }
    }

    suspend fun removeSession(){
        dataStore.updateData { UserPreferences.getDefaultInstance() }
    }

    private fun UserPreferences.toUser()= User(email, name, gender.toUser(), age)

    private fun User.toProto(): UserPreferences =
        UserPreferences.newBuilder()
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