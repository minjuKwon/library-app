package com.example.library.domain

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.google.firebase.firestore.ListenerRegistration

interface DatabaseRepository {
    suspend fun addLibraryBook(keyword:String, page: String, list:List<Library>):Result<Unit>
    suspend fun updateLibraryBook(
        libraryId:String,
        keyword:String,
        page: String,
        data: Map<String, Any>
    ):Result<Unit>
    suspend fun getLibraryBook(keyword:String, page: String):Result<List<Library>>
    suspend fun hasServerBook(keyword:String, page:String):Result<Boolean>
    suspend fun addLibraryLiked(libraryLiked: LibraryLiked):Result<Unit>
    suspend fun updateLibraryLiked(id:String, data: Map<String, Any>):Result<Unit>
    suspend fun getLibraryLikedList(userId:String):Result<List<LibraryLiked>>
    fun getLibraryLikedCount(bookId: String, onUpdate: (Int) -> Unit):ListenerRegistration
    suspend fun hasLibraryLiked(id:String):Result<Boolean>
    suspend fun addLoanHistory(libraryHistory: LibraryHistory):Result<Unit>
}