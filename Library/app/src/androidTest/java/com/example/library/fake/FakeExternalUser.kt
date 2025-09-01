package com.example.library.fake

import com.example.library.domain.ExternalUser

class FakeExternalUser(
    override val uid: String,
    override val isEmailVerified: Boolean= false,
    private var authList:MutableList<AuthUser> = mutableListOf()
) : ExternalUser {

    var isUserEmailVerified= isEmailVerified

    override suspend fun delete() {
        authList.removeIf{it.uid==uid}
    }

    override suspend fun sendEmailVerification() {
        isUserEmailVerified=true
    }

}