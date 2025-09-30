package com.example.library.data.repository

import com.example.library.data.FireStoreCollections.LIBRARY_COLLECTION
import com.example.library.data.FireStoreCollections.PAGE_NUMBER_COLLECTION
import com.example.library.data.FireStoreCollections.SEARCH_RESULTS_COLLECTION
import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.domain.DatabaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseBookRepository@Inject constructor(
    private val fireStore: FirebaseFirestore
): DatabaseRepository {

    override suspend fun addLibraryBook(keyword: String, page: String, list: List<Library>) {
        val batch = fireStore.batch()
        val normalizedQuery= normalizeQuery(keyword)

        list.forEach { item ->
            val docRef= fireStore.collection(SEARCH_RESULTS_COLLECTION)
                .document(normalizedQuery)
                .collection(PAGE_NUMBER_COLLECTION)
                .document(page)
                .collection(LIBRARY_COLLECTION)
                .document(item.libraryId)
            batch.set(docRef, item)
        }

        batch.commit().await()
    }

    override suspend fun getLibraryBook(keyword: String, page: String): List<Library> {
        val normalizedQuery= normalizeQuery(keyword)

        val snapshot= fireStore.collection(SEARCH_RESULTS_COLLECTION)
            .document(normalizedQuery)
            .collection(PAGE_NUMBER_COLLECTION)
            .document(page)
            .collection(LIBRARY_COLLECTION)
            .orderBy("offset")
            .get()
            .await()

        return snapshot.documents.map { doc ->
            doc.toObject(Library::class.java)!!
        }
    }

    override suspend fun hasServerBook(keyword: String, page: String): Boolean {
        val normalizedQuery= normalizeQuery(keyword)

        val snapshot = fireStore.collection(SEARCH_RESULTS_COLLECTION)
            .document(normalizedQuery)
            .collection(PAGE_NUMBER_COLLECTION)
            .document(page)
            .get()
            .await()

        return snapshot.exists()
    }

}