package com.example.library

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.ReservationStatusType
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.data.mapper.toStringType
import com.example.library.domain.DueCheckResult
import com.example.library.fake.DateConverter
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.exceptionRepository.LikeCheckFailingRepository
import com.example.library.fake.repository.exceptionRepository.ReservationByBookFailingRepository
import com.example.library.fake.repository.exceptionRepository.ReservationByUserFailingRepository
import com.example.library.fake.repository.exceptionRepository.GetLoanHistoryFailingRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.service.CheckLibraryLikeFailedException
import com.example.library.service.FirebaseBookService
import com.example.library.service.GetLoanDueStatusFailedException
import com.example.library.service.GetReservationsByBookFailedException
import com.example.library.service.GetReservationsByUserFailedException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirebaseBookServiceTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Test
    fun firebaseBookService_updateLibraryLiked_verifyCorrectValue_giveExistedValue()= runTest{
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository())

        fakeFirebaseBookService.updateLibraryLiked("1", "1",true)
        fakeFirebaseBookService.updateLibraryLiked("1", "1",false)

        val likedList= fakeFirebaseBookService.getLibraryLikedList("1")
        val result=likedList.getOrNull()
        val item= result!![0]

        assertEquals("1", item.bookId)
        assertFalse(item.isLiked)
    }

    @Test
    fun firebaseBookService_updateLibraryLiked_verifyCorrectValue_giveNoneExistedValue()= runTest{
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository())

        fakeFirebaseBookService.updateLibraryLiked("1", "1",true)

        val likedList= fakeFirebaseBookService.getLibraryLikedList("1")
        val result=likedList.getOrNull()
        val item= result!![0]

        assertEquals("1", item.bookId)
        assertTrue(item.isLiked)
    }

    @Test
    fun firebaseBookService_updateLibraryLiked_verifyFailure_hasLibraryLiked()= runTest{
        val fakeFirebaseBookService= FirebaseBookService(LikeCheckFailingRepository())

        assertFailsWith<CheckLibraryLikeFailedException>{
            fakeFirebaseBookService.updateLibraryLiked("", "",true)
        }
    }

    @Test
    fun firebaseBookService_getLoanDueStatus_verifyCorrectValue_giveEmptyList()= runTest {
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository())

        val status= fakeFirebaseBookService.getLoanDueStatus("","android","1")
        val result= status.getOrNull()!!
        assertEquals(DueCheckResult(), result)
    }

    @Test
    fun firebaseBookService_getLoanDueStatus_verifyCorrectValue_giveCorrectList()= runTest {
        val dateConverter=DateConverter()
        val fakeBookRepository= FakeBookRepository()
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Borrowed("", Instant.ofEpochMilli(0),Instant.ofEpochMilli(0)),
                    "",
                    "",
                    0
                )
            )
        )
        fakeBookRepository.addHistoryList(LibraryHistory(status = BookStatusType.BORROWED.name))
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(status = BookStatusType.BORROWED.name, dueDate = dateConverter.before())
        )
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(status = BookStatusType.BORROWED.name, dueDate = dateConverter.now)
        )
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(status = BookStatusType.BORROWED.name, dueDate = dateConverter.overdue())
        )

        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository)
        val status= fakeFirebaseBookService.getLoanDueStatus("","","")
        val result= status.getOrNull()!!

        assertEquals(1, result.before.size)
        assertEquals(1, result.today.size)
        assertEquals(1, result.overdue.size)

        assertEquals(
            BookStatusType.OVERDUE.name,
            fakeBookRepository.itemList[0].library.bookStatus.toStringType()
        )
        assertEquals(BookStatusType.OVERDUE.name, fakeBookRepository.historyList[0].status)
        assertEquals(BookStatusType.OVERDUE.name, fakeBookRepository.userLoanLibraryList[0].status)
    }

    @Test
    fun firebaseBookService_getLoanDueStatus_verifyFailure_getUserLoanBookList()= runTest {
        val fakeFirebaseBookService= FirebaseBookService(GetLoanHistoryFailingRepository())

        assertFailsWith<GetLoanDueStatusFailedException>{
            fakeFirebaseBookService.getLoanDueStatus("", "","")
        }
    }

    @Test
    fun firebaseBookService_getReservationList_verifyCorrectValue()= runTest {
        val fakeBookRepository= FakeBookRepository()
        fakeBookRepository.addReservationList(
            LibraryReservation(
                userId = "1",
                bookId = "1",
                bookTitle = "1",
                status = ReservationStatusType.WAITING.name
            )
        )
        fakeBookRepository.addReservationList(
            LibraryReservation(
                userId = "1",
                bookId = "2",
                bookTitle = "2",
                status = ReservationStatusType.WAITING.name
            )
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository)
        val list= fakeFirebaseBookService.getReservationList("1")
        val result= list.getOrNull()!!

        assertEquals(2, result.size)

        val row1= result[0]
        assertEquals("1", row1[0])
        assertEquals("1", row1[1])
        assertEquals("1", row1[3])
        assertEquals(ReservationStatusType.WAITING.ko, row1[4])

        val row2= result[1]
        assertEquals("2", row2[0])
        assertEquals("2", row2[1])
        assertEquals("1", row2[3])
        assertEquals(ReservationStatusType.WAITING.ko, row2[4])
    }

    @Test
    fun firebaseBookService_getReservationList_verifyFailure_getReservationsByBook()= runTest {
        val fakeFirebaseBookService=
            FirebaseBookService(ReservationByUserFailingRepository())

        assertFailsWith<GetReservationsByUserFailedException>{
            fakeFirebaseBookService.getReservationList("")
        }
    }

    @Test
    fun firebaseBookService_getReservationList_verifyFailure_getReservationsByUser()= runTest {
        val fakeFirebaseBookService=
            FirebaseBookService(ReservationByBookFailingRepository())

        assertFailsWith<GetReservationsByBookFailedException>{
            fakeFirebaseBookService.getReservationList("")
        }
    }

}