package com.example.library.data.entity

data class User(
    val email:String="",
    val name:String="",
    val gender: Gender = Gender.MALE,
    val age:Int=0
)

enum class Gender{
    MALE,FEMALE
}