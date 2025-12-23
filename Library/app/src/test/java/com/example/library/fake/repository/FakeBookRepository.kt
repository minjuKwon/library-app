package com.example.library.fake.repository

import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.ReservationStatusType
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.data.mapper.toStringType
import com.example.library.data.repository.FirebaseException
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.HistoryRequest
import com.example.library.ui.screens.user.getSuspensionEndDateToLong
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import java.time.Instant

class FakeBookRepository:DatabaseRepository {

    data class DatabaseItem(
        val userId: String="",
        val query:String,
        val page:String,
        val library: Library
    )

    val itemList= mutableListOf<DatabaseItem>()
    private val likeList= mutableListOf<LibraryLiked>()
    val historyList= mutableListOf<LibraryHistory>()
    val userLoanLibraryList= mutableListOf<UserLoanLibrary>()
    val reservationList= mutableListOf<LibraryReservation>()

    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            list.forEach { item ->
                val data= DatabaseItem("",normalizedQuery, page, item)
                itemList.add(data)
            }

            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            val list= itemList
                .filter { it.query==normalizedQuery && it.page==page }
                .sortedBy { it.library.offset }
                .map { it.library }

            Result.success(list)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            val isSave= itemList.any { it.query==normalizedQuery && it.page==page }

            Result.success(isSave)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        return try {
            likeList.add(libraryLiked)

            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        return try {
            val index= likeList.indexOfFirst { it.likedId==id }
            likeList[index]= likeList[index].copy(
                isLiked = (data["isLiked"] as Boolean),
                timestamp = (data["timestamp"] as Number).toLong()
            )
            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return try {
            Result.success(likeList)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        val count= likeList.count { it.bookId==bookId }
        onUpdate(count)

        return ListenerRegistration { }
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        return try {
            val hasLiked= likeList.any { it.likedId == id }

            Result.success(hasLiked)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        val status= historyList.filter { it.bookId==bookId }
            .sortedByDescending  { it.loanDate }
        callback(status.first())

        return ListenerRegistration { }
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        try{

            var history= LibraryHistory()
            var userLoan= UserLoanLibrary()
            var reservationBook= LibraryReservation()
            var reservationUser= LibraryReservation()

            if(historyRequest.bookStatus== BookStatusType.BORROWED.name||
                historyRequest.bookStatus== BookStatusType.OVERDUE.name){
                history= historyList.filter {
                    it.userId == historyRequest.userId
                            && it.bookId == historyRequest.bookId
                }.maxByOrNull { it.loanDate }?:LibraryHistory()
                userLoan= userLoanLibraryList.filter {
                    it.userId == historyRequest.userId
                            && it.bookId == historyRequest.bookId
                }.maxByOrNull { it.loanDate }?:UserLoanLibrary()
                reservationBook= reservationList.filter {
                    it.bookId == historyRequest.bookId
                            && it.status == ReservationStatusType.WAITING.name
                }.maxByOrNull { it.reservedAt }?: LibraryReservation()
            }else if(historyRequest.bookStatus== BookStatusType.RESERVED.name){
                reservationUser= reservationList.filter {
                    it.userId == historyRequest.userId
                }.maxByOrNull { it.reservedAt }?: LibraryReservation()
            }

            val hasOverdue= hasOverdueBook(historyRequest.userId)
            val hasOverdueResult= hasOverdue.getOrNull()

            val userLoanList= getUserLoanHistoryList(historyRequest.userId)
            val userLoanListResult= userLoanList.getOrNull()
            val suspensionDate= userLoanListResult?.getSuspensionEndDateToLong()

            val library= itemList.first {
                it.library.libraryId == historyRequest.libraryId
            }
            val bookStatus=library.library.bookStatus.toStringType()
            val userId= library.userId

            val libraryIdx= itemList.indexOfFirst {
                it.library.libraryId==library.library.libraryId
            }
            if(bookStatus == BookStatusType.AVAILABLE.name&&((hasOverdueResult!=null &&!hasOverdueResult)
                        &&(suspensionDate!=null&&historyRequest.eventDate>=suspensionDate))){
                val libraryHistory= LibraryHistory(
                    historyRequest.libraryHistoryId,
                    historyRequest.userId,
                    historyRequest.bookId,
                    BookStatusType.BORROWED.name,
                    historyRequest.eventDate,
                    historyRequest.dueDate
                )
                historyList.add(libraryHistory)

                val libraryData= library.copy(
                    userId = historyRequest.userId,
                    library = library.library.copy(
                        bookStatus = BookStatus.Borrowed(historyRequest.userId,
                            Instant.ofEpochMilli(historyRequest.eventDate),
                            Instant.ofEpochMilli(historyRequest.dueDate)
                        )
                    )
                )
                itemList[libraryIdx]= libraryData

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
                userLoanLibraryList.add(userLoanLibrary)
            }else if(bookStatus == BookStatusType.BORROWED.name){
                //반납하기
                if(userId== historyRequest.userId){
                    //다음 예약자가 있을 경우
                    val idx= reservationList.indexOf(reservationBook)
                    if(idx>=0){
                        reservationList[idx]= reservationList[idx].copy(
                            status = ReservationStatusType.NOTIFIED.name
                        )

                        itemList[libraryIdx]= itemList[libraryIdx].copy(
                            library = itemList[libraryIdx].library.copy(
                                bookStatus = BookStatus.Reserved,
                            )
                        )
                    }else{
                        //별도의 예약자가 없는 경우
                        itemList[libraryIdx]= itemList[libraryIdx].copy(
                            library = itemList[libraryIdx].library.copy(
                                bookStatus = BookStatus.Available,
                            )
                        )
                    }
                    val historyIdx= historyList.indexOf(history)
                    historyList[historyIdx]=historyList[historyIdx].copy(
                        status = BookStatusType.RETURNED.name,
                        returnDate = historyRequest.eventDate
                    )

                    val userIdx= userLoanLibraryList.indexOf(userLoan)
                    userLoanLibraryList[userIdx]= userLoanLibraryList[userIdx].copy(
                        status = BookStatusType.RETURNED.name,
                        returnDate = historyRequest.eventDate
                    )
                }else{
                    //BORROWED 상태에서 userId가 다르면 예약 로직
                    //예약 취소
                    if(reservationUser.status==ReservationStatusType.WAITING.name){
                        val idx= reservationList.indexOf(reservationUser)
                        reservationList[idx]=reservationList[idx].copy(
                            status = ReservationStatusType.CANCELLED.name
                        )
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
                        reservationList.add(libraryReservation)
                    }
                }
            }else if(bookStatus == BookStatusType.OVERDUE.name){
                if(userId==historyRequest.userId){
                    itemList[libraryIdx]=itemList[libraryIdx].copy(
                        library= itemList[libraryIdx].library.copy(
                            bookStatus= BookStatus.Available
                        )
                    )

                    val historyIdx= historyList.indexOf(history)
                    historyList[historyIdx]= historyList[historyIdx].copy(
                        status = BookStatusType.RETURNED.name,
                        returnDate = historyRequest.eventDate
                    )

                    val loanIdx= userLoanLibraryList.indexOf(userLoan)
                    userLoanLibraryList[loanIdx]= userLoanLibraryList[loanIdx].copy(
                        status = BookStatusType.RETURNED.name,
                        returnDate = historyRequest.eventDate
                    )
                }
            }else if(bookStatus==BookStatusType.RESERVED.name){
                //예약 당사자가 예약 차례가 되었을 때 대출
                if(reservationUser.status==ReservationStatusType.NOTIFIED.name){
                    val libraryHistory= LibraryHistory(
                        historyRequest.libraryHistoryId,
                        historyRequest.userId,
                        historyRequest.bookId,
                        BookStatusType.BORROWED.name,
                        historyRequest.eventDate,
                        historyRequest.dueDate
                    )
                    historyList.add(libraryHistory)

                    itemList[libraryIdx]= itemList[libraryIdx].copy(
                        userId= historyRequest.userId,
                        library = itemList[libraryIdx].library.copy(
                            bookStatus= BookStatus.Borrowed(
                                userId= historyRequest.userId,
                                borrowedAt = Instant.ofEpochMilli(historyRequest.eventDate),
                                dueDate = Instant.ofEpochMilli(historyRequest.dueDate)
                            )
                        )
                    )

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
                    userLoanLibraryList.add(userLoanLibrary)

                    val idx= reservationList.indexOf(reservationUser)
                    reservationList[idx] = reservationList[idx].copy(
                        status = ReservationStatusType.BORROWED.name
                    )
                }
            }
            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>?> {
        try{
            val list= userLoanLibraryList.filter {
                it.userId==userId
            }
            return if(list.isEmpty()) Result.success(null)
            else Result.success(list)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        try{
            val list= userLoanLibraryList.filter {
                it.userId==userId&& it.status== BookStatusType.RETURNED.name
            }.sortedByDescending { it.loanDate }

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
            val library = itemList.first {
                it.query== normalizedQuery
                        &&it.page== page
                        &&it.library.book.id==book.bookId
            }

            val history= historyList.first {
                it.userId==book.userId
                        &&it.bookId==book.bookId
                        &&it.status==BookStatusType.BORROWED.name
            }

            val loan= userLoanLibraryList.first {
                it.userId==book.userId
                        &&it.bookId==book.bookId
                        &&it.status==BookStatusType.BORROWED.name
            }
            if(library.userId==book.userId){
                val libraryIdx= itemList.indexOf(library)
                itemList[libraryIdx]= itemList[libraryIdx].copy(
                    library = itemList[libraryIdx].library.copy(
                        bookStatus = BookStatus.OverDue(
                            userId =library.userId,
                            overdueDate = Instant.ofEpochMilli(overdueDate)
                        )
                    )
                )

                val historyIdx= historyList.indexOf(history)
                historyList[historyIdx]= historyList[historyIdx].copy(
                    status = BookStatusType.OVERDUE.name
                )

                val loanIdx= userLoanLibraryList.indexOf(loan)
                userLoanLibraryList[loanIdx]= userLoanLibraryList[loanIdx].copy(
                    status = BookStatusType.OVERDUE.name
                )
            }
            return Result.success(Unit)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        try {
            val list= userLoanLibraryList.filter {
                it.userId==userId&&it.status==BookStatusType.OVERDUE.name
            }
            return Result.success(list.isNotEmpty())
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        try{
            val conditionList= listOf(ReservationStatusType.WAITING.name, ReservationStatusType.NOTIFIED.name)

            val list= reservationList.filter { reservation->
                reservation.bookId==bookId
                        &&conditionList.any { it==reservation.status }
            }
            return Result.success(list.isNotEmpty())
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        try{
            val list= reservationList.filter {
                it.userId==userId
                        &&it.status==ReservationStatusType.WAITING.name
            }
            return Result.success(list.isNotEmpty())
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        try{
            val list= reservationList.filter {
                it.bookId==bookId
                        &&it.status==ReservationStatusType.WAITING.name
            }

            return if(list.isEmpty()) Result.success(0)
            else Result.success(list.size)
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        try{
            val reservation= reservationList.filter {
                it.userId == userId
                        && it.bookId == bookId
            }.maxByOrNull { it.reservedAt }

            return Result.success(reservation?.status?:"")
        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        try{
            val list= reservationList.filter {
                it.userId==userId
                        &&it.status==ReservationStatusType.NOTIFIED.name
            }

            return if(list.isEmpty()) Result.success(emptyList())
            else Result.success(list)
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
            val list= reservationList.filter { reservation ->
                reservation.userId==userId
                        &&conditionList.any { reservation.status==it }
            }.sortedBy { it.reservedAt }

            return if(list.isEmpty()) Result.success(emptyList())
            else Result.success(list)
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
            val list= reservationList.filter { reservation ->
                reservation.bookId==bookId
                        &&conditionList.any { reservation.status==it }
            }.sortedBy { it.reservedAt }

            return if(list.isEmpty()) Result.success(emptyList())
            else Result.success(list)

        }catch (e: FirebaseFirestoreException){
            return Result.failure(FirebaseException(e.code.name))
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    fun addItemList(databaseItem: DatabaseItem){
        itemList.add(databaseItem)
    }

    fun addHistoryList(libraryHistory: LibraryHistory){
        historyList.add(libraryHistory)
    }

    fun addUserLoanBookList(userLoanLibrary: UserLoanLibrary){
        userLoanLibraryList.add(userLoanLibrary)
    }

    fun addReservationList(libraryReservation: LibraryReservation){
        reservationList.add(libraryReservation)
    }

}