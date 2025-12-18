package com.example.library.domain

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.google.firebase.firestore.ListenerRegistration

interface DatabaseRepository {
    suspend fun addLibraryBook(keyword:String, page: String, list:List<Library>):Result<Unit>
    suspend fun getLibraryBook(keyword:String, page: String):Result<List<Library>>
    suspend fun hasServerBook(keyword:String, page:String):Result<Boolean>
    suspend fun addLibraryLiked(libraryLiked: LibraryLiked):Result<Unit>
    suspend fun updateLibraryLiked(id:String, data: Map<String, Any>):Result<Unit>
    suspend fun getLibraryLikedList(userId:String):Result<List<LibraryLiked>>
    fun getLibraryLikedCount(bookId: String, onUpdate: (Int) -> Unit):ListenerRegistration
    suspend fun hasLibraryLiked(id:String):Result<Boolean>
    fun getLibraryStatus(bookId: String, callback: (LibraryHistory) -> Unit):ListenerRegistration
    suspend fun updateLibraryHistory(historyRequest: HistoryRequest):Result<Unit>
    suspend fun getUserLoanBookList(userId: String):Result<List<UserLoanLibrary>?>
    suspend fun getUserLoanHistoryList(userId: String):Result<List<UserLoanLibrary>>
    suspend fun updateUserOverdueBook(
        keyword:String,
        page: String,
        overdueDate:Long,
        book:UserLoanLibrary
    ):Result<Unit>
    suspend fun hasOverdueBook(userId: String):Result<Boolean>
    suspend fun hasReservedBook(bookId: String):Result<Boolean>
    suspend fun hasUserReservedBook(userId: String):Result<Boolean>
    suspend fun getLibraryReservationCount(bookId:String):Result<Int>
    suspend fun isMyReservationTurn(userId: String):Result<List<LibraryReservation>>
}

data class HistoryRequest(
    val userId:String,
    val libraryHistoryId:String,
    val libraryId:String,
    val bookId:String,
    val bookStatus:String,
    val bookTitle:String?,
    val bookAuthors:List<String>?,
    val keyword:String,
    val page: String,
    val eventDate:Long,
    val dueDate:Long
)