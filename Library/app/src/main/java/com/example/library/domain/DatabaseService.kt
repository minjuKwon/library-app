package com.example.library.domain

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.UserLoanLibrary
import com.google.firebase.firestore.ListenerRegistration

interface DatabaseService {
    suspend fun saveLibraryBooks(keyword:String, page: String, list:List<Library>):Result<Unit>
    suspend fun getLibraryBooks(keyword:String, page: String):Result<List<Library>>
    suspend fun isSavedBook(keyword:String, page:String):Result<Boolean>
    suspend fun updateLibraryLiked(userId:String, bookId:String, isLiked:Boolean):Result<List<LibraryLiked>>
    suspend fun getLibraryLikedList(userId:String):Result<List<LibraryLiked>>
    fun getLibraryLikedCount(bookId: String, onUpdate: (Int) -> Unit): ListenerRegistration
    fun getLibraryStatus(bookId: String, callback: (LibraryHistory) -> Unit):ListenerRegistration
    suspend fun updateLibraryHistory(
        userId:String,
        libraryId:String,
        bookId:String,
        bookStatus:String,
        bookTitle:String?,
        bookAuthors:List<String>?,
        keyword:String,
        page: String,
    ):Result<Unit>
    suspend fun getUserLoanBookList(userId: String):Result<List<UserLoanLibrary>>
}