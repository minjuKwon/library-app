package com.example.library.fake

import com.example.library.data.ExternalUser

class FakeExternalUser(
    override val uid: String = "",
    override val isEmailVerified:Boolean =false
): ExternalUser {
    override suspend fun delete() {
    }
    override suspend fun sendEmailVerification() {
    }
}