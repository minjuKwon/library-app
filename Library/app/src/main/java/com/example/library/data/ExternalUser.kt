package com.example.library.data

interface ExternalUser{
    val uid:String
    val isEmailVerified: Boolean
    suspend fun delete()
    suspend fun sendEmailVerification()
}