package com.example.library.service

import com.example.library.core.TimeProvider
import com.example.library.data.FireStoreField.IS_LIKED
import com.example.library.data.FireStoreField.TIMESTAMP
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.data.repository.FirebaseException
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.DatabaseService
import com.example.library.domain.HistoryRequest
import com.example.library.domain.LoanDateCalculator
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class FirebaseBookService@Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val timeProvider: TimeProvider,
    private val loanDateCalculator: LoanDateCalculator
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
        if(isExist.isFailure) throw isExist.exceptionOrNull()?:CheckLibraryLikeFailedException()

        val isExistResult= isExist.getOrNull()
        isExistResult?.let {
            if(it){
                databaseRepository.updateLibraryLiked(
                    id,
                    mapOf(IS_LIKED to isLiked, TIMESTAMP to now)
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

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        return databaseRepository.getLibraryStatus(bookId, callback)
    }

    override suspend fun updateLibraryHistory(
        userId: String,
        libraryId: String,
        bookId: String,
        bookStatus:String,
        bookTitle:String?,
        bookAuthors:List<String>?,
        keyword: String,
        page: String
    ): Result<Unit> {
        return try{
            val eventDate= timeProvider.now()
            val dueDate= loanDateCalculator.calculateDueDate(eventDate)

            val id="${userId}_${bookId}_${eventDate}"

            val historyRequest= HistoryRequest(
                userId= userId,
                libraryHistoryId= id,
                libraryId=libraryId,
                bookId= bookId,
                bookStatus= bookStatus,
                bookTitle= bookTitle,
                bookAuthors=bookAuthors,
                keyword=keyword,
                page=page,
                eventDate= eventDate,
                dueDate= dueDate
            )

            val isSave= databaseRepository.updateLibraryHistory(historyRequest)

            if(isSave.isSuccess){
                Result.success(Unit)
            }else{
                Result.failure(isSave.exceptionOrNull()?:UpdateLibraryStatusFailedException())
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getUserLoanBookList(
        userId: String
    ): Result<List<UserLoanLibrary>> {
        return databaseRepository.getUserLoanBookList(userId)
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        return databaseRepository.getUserLoanHistoryList(userId)
    }

}