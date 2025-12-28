package com.example.library.data.repository

import com.example.library.data.FireStoreCollections.LIBRARY_COLLECTION
import com.example.library.data.FireStoreCollections.LIBRARY_HISTORY_COLLECTION
import com.example.library.data.FireStoreCollections.LIBRARY_LIKED_COLLECTION
import com.example.library.data.FireStoreCollections.LIBRARY_RESERVATION_COLLECTION
import com.example.library.data.FireStoreCollections.PAGE_NUMBER_COLLECTION
import com.example.library.data.FireStoreCollections.SEARCH_RESULTS_COLLECTION
import com.example.library.data.FireStoreCollections.USER_LOAN_LIBRARY_COLLECTION
import com.example.library.data.FireStoreField.BOOK_ID
import com.example.library.data.FireStoreField.BOOK__ID
import com.example.library.data.FireStoreField.BORROWED_AT
import com.example.library.data.FireStoreField.DUE_DATE
import com.example.library.data.FireStoreField.IS_LIKED
import com.example.library.data.FireStoreField.LOAN_DATE
import com.example.library.data.FireStoreField.OFFSET
import com.example.library.data.FireStoreField.OVERDUE_DATE
import com.example.library.data.FireStoreField.RESERVED_AT
import com.example.library.data.FireStoreField.RETURN_DATE
import com.example.library.data.FireStoreField.STATUS
import com.example.library.data.FireStoreField.STATUS_TYPE
import com.example.library.data.FireStoreField.USER_ID
import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.ReservationStatusType
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.data.firebase.LibraryFirebaseDto
import com.example.library.data.mapper.toFirebaseDto
import com.example.library.data.mapper.toLibrary
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.HistoryRequest
import com.example.library.ui.screens.user.getSuspensionEndDateToLong
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
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
            return Result.failure (FirebaseException(e.code.name))
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
                .orderBy(OFFSET)
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
                .collection(LIBRARY_COLLECTION)
                .get()
                .await()

            val isExists= snapshot.documents.isNotEmpty()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        try{
            fireStore.collection(LIBRARY_LIKED_COLLECTION)
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
            fireStore.collection(LIBRARY_LIKED_COLLECTION)
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
            val snapshot= fireStore.collection(LIBRARY_LIKED_COLLECTION)
                .whereEqualTo(USER_ID, userId)
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
        val query = fireStore.collection(LIBRARY_LIKED_COLLECTION)
            .whereEqualTo(BOOK_ID,bookId)
            .whereEqualTo(IS_LIKED,true)

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
            val snapshot = fireStore.collection(LIBRARY_LIKED_COLLECTION)
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

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        val query = fireStore.collection(LIBRARY_HISTORY_COLLECTION)
            .whereEqualTo(BOOK_ID,bookId)
            .orderBy(LOAN_DATE, Query.Direction.DESCENDING)

        return query.addSnapshotListener { snapshot, error ->
            if (snapshot==null || error != null) {
                return@addSnapshotListener
            }

            val data= snapshot.documents.firstOrNull()?.toObject(LibraryHistory::class.java)
                ?:return@addSnapshotListener
            callback(data)
        }
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        try{

            var historyDocRef: DocumentReference? =null
            var userLoanBookDocRef:DocumentReference? = null
            var reservationUserDocRef:DocumentReference?= null
            var reservationUserSnap:DocumentSnapshot?=null
            var reservationBookSnap: QuerySnapshot?= null
            var reservationBookDocRef:DocumentReference?= null

            var userReservedStatus=""

            if(historyRequest.bookStatus== BookStatusType.BORROWED.name||
                historyRequest.bookStatus== BookStatusType.OVERDUE.name){
                historyDocRef= fireStore.collection(LIBRARY_HISTORY_COLLECTION)
                    .whereEqualTo(USER_ID, historyRequest.userId)
                    .whereEqualTo(BOOK_ID,historyRequest.bookId)
                    .orderBy(LOAN_DATE, Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .first()
                    .reference

                userLoanBookDocRef= fireStore.collection(USER_LOAN_LIBRARY_COLLECTION)
                    .whereEqualTo(USER_ID, historyRequest.userId)
                    .whereEqualTo(BOOK_ID,historyRequest.bookId)
                    .orderBy(LOAN_DATE, Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .first()
                    .reference

                reservationBookSnap= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                    .whereEqualTo(BOOK_ID,historyRequest.bookId)
                    .whereEqualTo(STATUS, ReservationStatusType.WAITING.name)
                    .orderBy(RESERVED_AT, Query.Direction.DESCENDING)
                    .get()
                    .await()
                if(!reservationBookSnap.isEmpty){
                    reservationBookDocRef= reservationBookSnap.documents.first().reference
                }
            }else if(historyRequest.bookStatus== BookStatusType.RESERVED.name){
                reservationUserDocRef= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                    .whereEqualTo(USER_ID, historyRequest.userId)
                    .orderBy(RESERVED_AT, Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .first()
                    .reference
                reservationUserSnap= reservationUserDocRef.get().await()
            }
            userReservedStatus=
                getReservedStatus(historyRequest.userId, historyRequest.bookId).getOrNull()?:""

            val hasOverdue= hasOverdueBook(historyRequest.userId)
            val hasOverdueResult= hasOverdue.getOrNull()

            val userLoanList= getUserLoanHistoryList(historyRequest.userId)
            val userLoanListResult= userLoanList.getOrNull()

            fireStore.runTransaction { transaction ->
                val normalizedQuery= normalizeQuery(historyRequest.keyword)

                val libraryDocRef= fireStore.collection(SEARCH_RESULTS_COLLECTION)
                    .document(normalizedQuery)
                    .collection(PAGE_NUMBER_COLLECTION)
                    .document(historyRequest.page)
                    .collection(LIBRARY_COLLECTION)
                    .document(historyRequest.libraryId)
                val librarySnap= transaction.get(libraryDocRef)

                val status= librarySnap.get(STATUS_TYPE)
                val userId= librarySnap.get(USER_ID)

                val suspensionDate= userLoanListResult?.getSuspensionEndDateToLong()
                if(status == BookStatusType.AVAILABLE.name&&((hasOverdueResult!=null &&!hasOverdueResult)
                            &&(suspensionDate!=null&&historyRequest.eventDate>=suspensionDate))){
                    val libraryHistory= LibraryHistory(
                        historyRequest.libraryHistoryId,
                        historyRequest.userId,
                        historyRequest.bookId,
                        BookStatusType.BORROWED.name,
                        historyRequest.eventDate,
                        historyRequest.dueDate
                    )
                    val loanDocRef= fireStore.collection(LIBRARY_HISTORY_COLLECTION)
                        .document(historyRequest.libraryHistoryId)
                    transaction.set(loanDocRef, libraryHistory)

                    val data= mapOf(
                        USER_ID to historyRequest.userId,
                        STATUS_TYPE to BookStatusType.BORROWED.name,
                        BORROWED_AT to historyRequest.eventDate,
                        DUE_DATE to historyRequest.dueDate
                    )
                    transaction.update(libraryDocRef, data)

                    val userLoanLibrary= UserLoanLibrary(
                        userLibraryInfoId = historyRequest.libraryHistoryId,
                        userId = historyRequest.userId,
                        bookId = historyRequest.bookId,
                        title = historyRequest.bookTitle,
                        authors = historyRequest.bookAuthors,
                        status = BookStatusType.BORROWED.name,
                        loanDate = historyRequest.eventDate,
                        dueDate = historyRequest.dueDate
                    )
                    val userLoanDocRef= fireStore.collection(USER_LOAN_LIBRARY_COLLECTION)
                        .document(historyRequest.libraryHistoryId)
                    transaction.set(userLoanDocRef, userLoanLibrary)
                }else if(status == BookStatusType.BORROWED.name){
                    //반납하기
                    if(userId== historyRequest.userId){
                        //다음 예약자가 있을 경우
                        if(reservationBookSnap?.isEmpty==false){
                            if (reservationBookDocRef != null) {
                                transaction.update(
                                    reservationBookDocRef,
                                    mapOf(STATUS to ReservationStatusType.NOTIFIED.name)
                                )

                                val libraryData= mapOf(
                                    STATUS_TYPE to BookStatusType.RESERVED.name,
                                    BORROWED_AT to null,
                                    DUE_DATE to null
                                )
                                transaction.update(libraryDocRef, libraryData)
                            }
                        }else{
                            //별도의 예약자가 없는 경우
                            val libraryData= mapOf(
                                STATUS_TYPE to BookStatusType.AVAILABLE.name,
                                BORROWED_AT to null,
                                DUE_DATE to null
                            )
                            transaction.update(libraryDocRef, libraryData)
                        }
                        val historyData= mapOf(
                            STATUS to BookStatusType.RETURNED.name,
                            RETURN_DATE to historyRequest.eventDate
                        )
                        if (historyDocRef != null) {
                            transaction.update(historyDocRef, historyData)
                        }

                        val userLoanBookData= mapOf(
                            STATUS to BookStatusType.RETURNED.name,
                            RETURN_DATE to historyRequest.eventDate
                        )
                        if (userLoanBookDocRef != null) {
                            transaction.update(userLoanBookDocRef, userLoanBookData)
                        }
                    }else{
                        //BORROWED 상태에서 userId가 다르면 예약 로직
                        if(reservationUserDocRef!=null){
                            //예약 취소
                            if(reservationUserSnap?.exists() == true){
                                if(reservationUserSnap.get(STATUS) ==ReservationStatusType.WAITING.name){
                                    val reservationData=
                                        mapOf(STATUS to ReservationStatusType.CANCELLED.name)
                                    transaction.update(reservationUserDocRef, reservationData)
                                }
                            }
                        }else{
                            //예약 생성
                            val libraryReservation= LibraryReservation(
                                historyRequest.libraryHistoryId,
                                historyRequest.userId,
                                historyRequest.bookId,
                                historyRequest.bookTitle?:"",
                                historyRequest.eventDate,
                                ReservationStatusType.WAITING.name
                            )
                            val reservationNewDocRef= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                                .document(historyRequest.libraryHistoryId)
                            transaction.set(reservationNewDocRef, libraryReservation)
                        }
                    }
                }else if(status == BookStatusType.OVERDUE.name){
                    if(userId== historyRequest.userId){
                        val libraryData= mapOf(
                            STATUS_TYPE to BookStatusType.AVAILABLE.name,
                            BORROWED_AT to null,
                            DUE_DATE to null,
                            OVERDUE_DATE to null
                        )
                        transaction.update(libraryDocRef, libraryData)

                        val historyData= mapOf(
                            STATUS to BookStatusType.RETURNED.name,
                            RETURN_DATE to historyRequest.eventDate
                        )
                        if (historyDocRef != null) {
                            transaction.update(historyDocRef, historyData)
                        }

                        val userLoanBookData= mapOf(
                            STATUS to BookStatusType.RETURNED.name,
                            RETURN_DATE to historyRequest.eventDate
                        )
                        if (userLoanBookDocRef != null) {
                            transaction.update(userLoanBookDocRef, userLoanBookData)
                        }
                    }else{
                        return@runTransaction
                    }
                }else if(status==BookStatusType.RESERVED.name){
                    //예약 당사자가 예약 차례가 되었을 때 대출
                    if(userReservedStatus==ReservationStatusType.NOTIFIED.name){
                        val libraryHistory= LibraryHistory(
                            historyRequest.libraryHistoryId,
                            historyRequest.userId,
                            historyRequest.bookId,
                            BookStatusType.BORROWED.name,
                            historyRequest.eventDate,
                            historyRequest.dueDate
                        )
                        val loanDocRef= fireStore.collection(LIBRARY_HISTORY_COLLECTION)
                            .document(historyRequest.libraryHistoryId)
                        transaction.set(loanDocRef, libraryHistory)

                        val data= mapOf(
                            USER_ID to historyRequest.userId,
                            STATUS_TYPE to BookStatusType.BORROWED.name,
                            BORROWED_AT to historyRequest.eventDate,
                            DUE_DATE to historyRequest.dueDate
                        )
                        transaction.update(libraryDocRef, data)

                        val userLoanLibrary= UserLoanLibrary(
                            userLibraryInfoId = historyRequest.libraryHistoryId,
                            userId = historyRequest.userId,
                            bookId = historyRequest.bookId,
                            title = historyRequest.bookTitle,
                            authors = historyRequest.bookAuthors,
                            status = BookStatusType.BORROWED.name,
                            loanDate = historyRequest.eventDate,
                            dueDate = historyRequest.dueDate
                        )
                        val userLoanDocRef= fireStore.collection(USER_LOAN_LIBRARY_COLLECTION)
                            .document(historyRequest.libraryHistoryId)
                        transaction.set(userLoanDocRef, userLoanLibrary)

                        if(reservationUserDocRef!=null){
                            transaction.update(
                                reservationUserDocRef,
                                mapOf(STATUS to ReservationStatusType.BORROWED.name)
                            )
                        }
                    }
                }
                else{
                    return@runTransaction
                }
            }.await()
            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getUserLoanBookList(
        userId: String
    ): Result<List<UserLoanLibrary>?> {
        try{
            val snapshot= getUserLoanList(userId)
                .get()
                .await()

            if(snapshot.isEmpty){
                return Result.success(null)
            }else{
                val list= snapshot.documents.map { doc ->
                    doc.toObject(UserLoanLibrary::class.java)!!
                }
                return Result.success(list)
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        try{
            val snapshot= getUserLoanList(userId)
                .whereEqualTo(STATUS, BookStatusType.RETURNED.name)
                .orderBy(LOAN_DATE, Query.Direction.DESCENDING)
                .get()
                .await()

            val list= snapshot.documents.map { doc ->
                doc.toObject(UserLoanLibrary::class.java)!!
            }

            return Result.success(list)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun updateUserOverdueBook(
        keyword: String,
        page: String,
        overdueDate: Long,
        book: UserLoanLibrary
    ): Result<Unit> {
        try{
            val normalizedQuery= normalizeQuery(keyword)
            val libraryDocRef= fireStore.collection(SEARCH_RESULTS_COLLECTION)
                .document(normalizedQuery)
                .collection(PAGE_NUMBER_COLLECTION)
                .document(page)
                .collection(LIBRARY_COLLECTION)
                .whereEqualTo(BOOK__ID, book.bookId)
                .get()
                .await()
                .documents
                .first()
                .reference

            val historyDocRef= fireStore.collection(LIBRARY_HISTORY_COLLECTION)
                .whereEqualTo(USER_ID, book.userId)
                .whereEqualTo(BOOK_ID, book.bookId)
                .whereEqualTo(STATUS, BookStatusType.BORROWED.name)
                .get()
                .await()
                .documents
                .first()
                .reference

            val userLoanBookDocRef= fireStore.collection(USER_LOAN_LIBRARY_COLLECTION)
                .whereEqualTo(USER_ID, book.userId)
                .whereEqualTo(BOOK_ID, book.bookId)
                .whereEqualTo(STATUS, BookStatusType.BORROWED.name)
                .get()
                .await()
                .documents
                .first()
                .reference

            fireStore.runTransaction { transaction ->
                val librarySnap= transaction.get(libraryDocRef)
                val userId= librarySnap.get(USER_ID)

                if(userId== book.userId){
                    val libraryData= mapOf(
                        STATUS_TYPE to BookStatusType.OVERDUE.name,
                        OVERDUE_DATE to overdueDate
                    )
                    transaction.update(libraryDocRef, libraryData)

                    val historyData= mapOf(
                        STATUS to BookStatusType.OVERDUE.name
                    )
                    transaction.update(historyDocRef, historyData)

                    val userLoanBookData= mapOf(
                        STATUS to BookStatusType.OVERDUE.name
                    )
                    transaction.update(userLoanBookDocRef, userLoanBookData)
                }else{
                    return@runTransaction
                }
            }
            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        try{
            val snapshot= getUserLoanList(userId)
                .whereEqualTo(STATUS, BookStatusType.OVERDUE.name)
                .get()
                .await()

            val isExists= snapshot.documents.isNotEmpty()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        try{
            val conditionList= listOf(ReservationStatusType.WAITING.name, ReservationStatusType.NOTIFIED.name)
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(BOOK_ID, bookId)
                .whereIn(STATUS, conditionList)
                .get()
                .await()

            val isExists= snapshot.documents.isNotEmpty()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        try{
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .whereEqualTo(STATUS, ReservationStatusType.WAITING.name)
                .get()
                .await()

            val isExists= snapshot.documents.isNotEmpty()

            return Result.success(isExists)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        try{
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(BOOK_ID, bookId)
                .whereEqualTo(STATUS, ReservationStatusType.WAITING.name)
                .get()
                .await()

            return if(snapshot.isEmpty){
                Result.success(0)
            }else{
                Result.success(snapshot.documents.count())
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        try{
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .whereEqualTo(BOOK_ID, bookId)
                .orderBy(RESERVED_AT, Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .first()

                val status=snapshot.getString(STATUS)
            return Result.success(status?:"")
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        try{
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .whereEqualTo(STATUS, ReservationStatusType.NOTIFIED.name)
                .get()
                .await()

            if(snapshot.isEmpty){
                return Result.success(emptyList())
            }else{
                val list= snapshot.documents.map { doc ->
                    doc.toObject(LibraryReservation::class.java)?:LibraryReservation()
                }
                return Result.success(list)
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<LibraryReservation>> {
        try{
            val conditionList= listOf(
                ReservationStatusType.WAITING.name,
                ReservationStatusType.NOTIFIED.name,
                ReservationStatusType.CANCELLED.name
            )
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .whereIn(STATUS, conditionList)
                .orderBy(RESERVED_AT, Query.Direction.ASCENDING)
                .get()
                .await()

            if(snapshot.isEmpty){
                return Result.success(emptyList())
            }else{
                val list= snapshot.documents.map { doc ->
                    doc.toObject(LibraryReservation::class.java)?:LibraryReservation()
                }
                return Result.success(list)
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getReservationsByBook(bookId: String): Result<List<LibraryReservation>> {
        try{
            val conditionList= listOf(
                ReservationStatusType.WAITING.name,
                ReservationStatusType.NOTIFIED.name,
            )
            val snapshot= fireStore.collection(LIBRARY_RESERVATION_COLLECTION)
                .whereEqualTo(BOOK_ID, bookId)
                .whereIn(STATUS, conditionList)
                .orderBy(RESERVED_AT, Query.Direction.ASCENDING)
                .get()
                .await()

            if(snapshot.isEmpty){
                return Result.success(emptyList())
            }else{
                val list= snapshot.documents.map { doc ->
                    doc.toObject(LibraryReservation::class.java)?:LibraryReservation()
                }
                return Result.success(list)
            }
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    private fun getUserLoanList(userId: String): Query{
        return fireStore.collection(USER_LOAN_LIBRARY_COLLECTION)
            .whereEqualTo(USER_ID, userId)
    }

}