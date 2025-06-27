package com.example.library.data

data class User(
    val email:String="",
    val name:String="",
    val gender:Gender=Gender.MALE,
    val age:Int=0
)

enum class Gender{
    MALE,FEMALE
}