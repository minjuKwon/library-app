package com.example.library.data.entity

import com.google.firebase.firestore.PropertyName

data class LibraryLiked(
    val likedId:String="",
    val userId:String="",
    val bookId:String="",
    @get:PropertyName("isLiked")
    val isLiked:Boolean=false,
    val timestamp:Long=0
)