package com.example.library.data.repository

import com.example.library.data.FireStoreCollections.LIBRARY_COLLECTION
import com.example.library.data.FireStoreCollections.LIBRARY_HISTORY
import com.example.library.data.FireStoreCollections.LIBRARY_LIKED
import com.example.library.data.FireStoreCollections.PAGE_NUMBER_COLLECTION
import com.example.library.data.FireStoreCollections.SEARCH_RESULTS_COLLECTION
import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.firebase.LibraryFirebaseDto
import com.example.library.data.mapper.toFirebaseDto
import com.example.library.data.mapper.toLibrary
import com.example.library.domain.DatabaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseBookRepository@Inject constructor(
    private val fireStore: FirebaseFirestore
): DatabaseRepository {

    override suspend fun addLibraryBook(keyword: String, page: String, list: List<Library>): Result<Unit> {
        try{
            val batch = fireStore.batch()
            val normalizedQuery= normalizeQuery(keyword)

            list.forEach { item ->
                val data= item.toFirebaseDto()
                val docRef= fireStore.collection(SEARCH_RESULTS_COLLECTION)
                    .document(normalizedQuery)
                    .collection(PAGE_NUMBER_COLLECTION)
                    .document(page)
                    .collection(LIBRARY_COLLECTION)
                    .document(data.libraryId)
                batch.set(docRef, data)
            }

            batch.commit().await()

            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun updateLibraryBook(
        libraryId:String,
        keyword: String,
        page: String,
        data: Map<String, Any>
    ): Result<Unit> {
        try{
            val normalizedQuery= normalizeQuery(keyword)

            fireStore.collection(SEARCH_RESULTS_COLLECTION)
                .document(normalizedQuery)
                .collection(PAGE_NUMBER_COLLECTION)
                .document(page)
                .collection(LIBRARY_COLLECTION)
                .document(libraryId)
                .update(data)
                .await()
            Result.success(Unit)

            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        try{
            val normalizedQuery= normalizeQuery(keyword)

            val snapshot= fireStore.collection(SEARCH_RESULTS_COLLECTION)
                .document(normalizedQuery)
                .collection(PAGE_NUMBER_COLLECTION)
                .document(page)
                .collection(LIBRARY_COLLECTION)
                .orderBy("offset")
                .get()
                .await()

            val list= snapshot.documents.map { doc ->
                doc.toObject(LibraryFirebaseDto::class.java)!!.toLibrary()
            }
            return Result.success(list)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        try {
            val normalizedQuery= normalizeQuery(keyword)

            val snapshot = fireStore.collection(SEARCH_RESULTS_COLLECTION)
                .document(normalizedQuery)
                .collection(PAGE_NUMBER_COLLECTION)
                .document(page)
                .get()
                .await()

            val isExists= snapshot.exists()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        try{
            fireStore.collection(LIBRARY_LIKED)
                .document(libraryLiked.likedId)
                .set(libraryLiked)
                .await()

            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        try{
            fireStore.collection(LIBRARY_LIKED)
                .document(id)
                .update(data)
                .await()
            Result.success(Unit)

            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        try{
            val snapshot= fireStore.collection(LIBRARY_LIKED)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val list= snapshot.documents.map { doc ->
                doc.toObject(LibraryLiked::class.java)!!
            }

            return Result.success(list)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        val query = fireStore.collection(LIBRARY_LIKED)
            .whereEqualTo("bookId",bookId)
            .whereEqualTo("isLiked",true)

        return query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val likeCount = snapshot?.documents?.count () ?: -1
            onUpdate(likeCount)
        }
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        try{
            val snapshot = fireStore.collection(LIBRARY_LIKED)
                .document(id)
                .get()
                .await()

            val isExists= snapshot.exists()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun addLoanHistory(libraryHistory: LibraryHistory): Result<Unit> {
        try{
            fireStore.collection(LIBRARY_HISTORY)
                .document(libraryHistory.loanHistoryId)
                .set(libraryHistory)
                .await()

            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

}