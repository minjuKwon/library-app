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
import com.example.library.fake.FakeSessionManager
import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.service.FirebaseBookService
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibraryDetailsViewModelTest {

    private lateinit var testScope:CoroutineScope

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @After
    fun closeResource(){
        testScope.cancel()
    }

    //이용자가 도서를 대출 중인 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusBorrowed_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //대출 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Available,
                    "",
                    "",
                    0
                )
            )
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getBookStatus(true)

        testScheduler.advanceUntilIdle()

        assertEquals(
            BookStatusType.BORROWED.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //이용자가 대출 중인 도서를 반납한 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusReturned_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //대출 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Available,
                    "",
                    "",
                    0
                )
            )
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        //대출
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getBookStatus(true)
        testScheduler.advanceUntilIdle()
        
        //반납
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getBookStatus(true)
        testScheduler.advanceUntilIdle()

        assertEquals(
            BookStatusType.AVAILABLE.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //다른 사람 대출하여 이용자가 예약한 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusMyReservation_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //대출된 사용자 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                userId = "other",
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Borrowed("", Instant.ofEpochMilli(0), Instant.ofEpochMilli(0)),
                    "",
                    "",
                    0
                )
            )
        )
        fakeBookRepository.addHistoryList(LibraryHistory(userId="other",status = BookStatusType.BORROWED.name))
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(userId="other",status = BookStatusType.BORROWED.name, dueDate = 0)
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.currentLibrary.value= Library(
            "",
            Book(),
            BookStatus.Borrowed("", Instant.ofEpochMilli(0), Instant.ofEpochMilli(0)),
            "",
            "",
            0
        )
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getReservationCount(true)
        detailsViewModel.getBookStatus(true)

        testScheduler.advanceUntilIdle()

        assertEquals(
            BookStatusType.RESERVED.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //다른 사람 대출하여 이용자가 예약한 도서를 취소된 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusMyReservationCancel_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //대출된 사용자 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                userId = "other",
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Borrowed("", Instant.ofEpochMilli(0), Instant.ofEpochMilli(0)),
                    "",
                    "",
                    0
                )
            )
        )
        fakeBookRepository.addHistoryList(LibraryHistory(userId="other",status = BookStatusType.BORROWED.name))
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(userId="other",status = BookStatusType.BORROWED.name, dueDate = 0)
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.currentLibrary.value= Library(
            "",
            Book(),
            BookStatus.Borrowed("", Instant.ofEpochMilli(0), Instant.ofEpochMilli(0)),
            "",
            "",
            0
        )

        //예약
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getReservationCount(true)
        detailsViewModel.getBookStatus(true)
        testScheduler.advanceUntilIdle()

        //취소
        detailsViewModel.loanLibrary("","")
        detailsViewModel.getBookStatus(true)
        testScheduler.advanceUntilIdle()

        assertEquals(
            BookStatusType.UNAVAILABLE.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //다른 사람 대출 하였지만 또 다른 사람이 예약한 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusOtherReservation_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //대출된 사용자 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                userId = "other",
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Borrowed("", Instant.ofEpochMilli(0), Instant.ofEpochMilli(0)),
                    "",
                    "",
                    0
                )
            )
        )
        fakeBookRepository.addHistoryList(LibraryHistory(userId="other",status = BookStatusType.BORROWED.name))
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(userId="other",status = BookStatusType.BORROWED.name, dueDate = 0)
        )
        //예약 데이터 추가
        fakeBookRepository.addReservationList(
            LibraryReservation(userId="other2",status = ReservationStatusType.WAITING.name)
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.getReservationCount(true)
        detailsViewModel.getBookStatus(true)

        testScheduler.advanceUntilIdle()

        assertEquals(1, detailsViewModel.reservationCount.value[""])
        assertEquals(
            BookStatusType.UNAVAILABLE.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //반납 되었지만 예약 되어서 대출 되기를 기다리는 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusWaitingReservation_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //반납된 사용자 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                userId = "other",
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Available,
                    "",
                    "",
                    0
                )
            )
        )
        fakeBookRepository.addHistoryList(LibraryHistory(userId="other",status = BookStatusType.RETURNED.name))
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(userId="other",status = BookStatusType.RETURNED.name)
        )
        //예약 데이터 추가
        fakeBookRepository.addReservationList(
            LibraryReservation(userId="other2",status = ReservationStatusType.NOTIFIED.name)
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.getReservationCount(true)
        detailsViewModel.getBookStatus(true)

        testScheduler.advanceUntilIdle()

        assertEquals(
            BookStatusType.RESERVED.name,
            detailsViewModel.currentLibrary.value.bookStatus.toStringType()
        )
    }

    //연체된 도서 상태 테스트
    @Test
    fun detailsViewModel_getBookStatusOverdue_verifyCorrectValue()= runTest {
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        //연체된 사용자 데이터 추가
        fakeBookRepository.addItemList(
            FakeBookRepository.DatabaseItem(
                userId = "user1",
                query = "",
                page = "",
                library = Library(
                    "",
                    Book(),
                    BookStatus.Available,
                    "",
                    "",
                    0
                )
            )
        )
        val now=System.currentTimeMillis()
        fakeBookRepository.addHistoryList(
            LibraryHistory(
                userId = "user1", status = BookStatusType.OVERDUE.name,
                dueDate = now*Integer.MAX_VALUE,
                returnDate = now*2
            )
        )
        fakeBookRepository.addUserLoanBookList(
            UserLoanLibrary(
                userId = "user1",
                status = BookStatusType.OVERDUE.name,
                dueDate = now*Integer.MAX_VALUE,
                returnDate = now*2
            )
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val detailsViewModel = LibraryDetailsViewModel(
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )
        detailsViewModel.currentLibrary.value= Library(
            "",
            Book(),
            BookStatus.Available,
            "",
            "",
            0
        )
        detailsViewModel.loanLibrary("","")

        testScheduler.advanceUntilIdle()

        assertTrue(detailsViewModel.isShowOverdueDialog.value)
        assertTrue(detailsViewModel.isShowSuspensionDateDialog.value)
    }

}