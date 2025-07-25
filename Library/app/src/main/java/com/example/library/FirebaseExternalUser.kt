package com.example.library

import com.example.library.data.ExternalUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseExternalUser(private val user: FirebaseUser):ExternalUser {
    override val uid = user.uid
    override val isEmailVerified = user.isEmailVerified

    override suspend fun delete() {
        user.delete().await()
    }

    override suspend fun sendEmailVerification() {
        user.sendEmailVerification().await()
    }
}