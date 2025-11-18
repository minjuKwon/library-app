package com.example.library.service

import com.example.library.core.TimeProvider
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryLiked
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.DatabaseService
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class FirebaseBookService@Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val timeProvider: TimeProvider
):DatabaseService {

    override suspend fun saveLibraryBooks(keyword: String, page: String, list: List<Library>): Result<Unit> {
        return databaseRepository.addLibraryBook(keyword,page,list)
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        return databaseRepository.getLibraryBook(keyword,page)
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return databaseRepository.hasServerBook(keyword,page)
    }

    override suspend fun updateLibraryLiked(userId:String, bookId:String, isLiked:Boolean):Result<List<LibraryLiked>> {
        val id="${userId}_${bookId}"
        val now= timeProvider.now()

        val isExist= databaseRepository.hasLibraryLiked(id)
        if(isExist.isFailure) throw isExist.exceptionOrNull()?:Exception()

        val isExistResult= isExist.getOrNull()
        isExistResult?.let {
            if(it){
                databaseRepository.updateLibraryLiked(
                    id,
                    mapOf("isLiked" to isLiked, "timestamp" to now)
                )
            }else{
                val libraryLiked= LibraryLiked(id, userId, bookId, isLiked, now)
                databaseRepository.addLibraryLiked(libraryLiked)
            }
        }

        return databaseRepository.getLibraryLikedList(userId)
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return databaseRepository.getLibraryLikedList(userId)
    }

    override fun getLibraryLikedCount(bookId: String, onUpdate: (Int) -> Unit): ListenerRegistration {
        return databaseRepository.getLibraryLikedCount(bookId, onUpdate)
    }

}