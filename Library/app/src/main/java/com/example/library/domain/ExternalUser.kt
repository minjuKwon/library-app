package com.example.library.domain

interface ExternalUser{
    val uid:String
    val isEmailVerified: Boolean
    suspend fun delete()
    suspend fun sendEmailVerification()
}