package com.example.library.service

import com.example.library.core.TimeProvider
import com.example.library.data.FireStoreField.BORROWED_AT
import com.example.library.data.FireStoreField.DUE_DATE
import com.example.library.data.FireStoreField.IS_LIKED
import com.example.library.data.FireStoreField.STATUS_TYPE
import com.example.library.data.FireStoreField.TIMESTAMP
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.repository.FirebaseException
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.DatabaseService
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

    override suspend fun saveLoanHistory(
        userId: String,
        keyword: String,
        page: String,
        libraryId: String,
        bookId: String
    ): Result<Unit> {
        return try{
            val loanDate= timeProvider.now()
            val dueDate= loanDateCalculator.calculateDueDate(loanDate)

            val id="${userId}_${bookId}_${loanDate}"

            val isSave= databaseRepository.addLoanHistory(
                LibraryHistory(id, userId, bookId, BookStatusType.BORROWED.name, loanDate, dueDate)
            )

            if(isSave.isSuccess){
                databaseRepository.updateLibraryBook(
                    libraryId,
                    keyword,
                    page,
                    mapOf(
                        STATUS_TYPE to BookStatusType.BORROWED.name,
                        BORROWED_AT to loanDate,
                        DUE_DATE to dueDate
                    )
                )
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

    override fun getLibraryStatus(
        bookId: String,
        callback: (BookStatus) -> Unit
    ): ListenerRegistration {
        return databaseRepository.getLibraryStatus(bookId, callback)
    }

}