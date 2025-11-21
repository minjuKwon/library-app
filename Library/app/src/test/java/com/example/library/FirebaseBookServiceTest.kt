package com.example.library

import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.FakeExceptionBookRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.service.CheckLibraryLikeFailedException
import com.example.library.service.FirebaseBookService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirebaseBookServiceTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Test
    fun firebaseBookService_updateLibraryLiked_verifyCorrectValue_giveExistedValue()= runTest{
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())

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
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())

        fakeFirebaseBookService.updateLibraryLiked("1", "1",true)

        val likedList= fakeFirebaseBookService.getLibraryLikedList("1")
        val result=likedList.getOrNull()
        val item= result!![0]

        assertEquals("1", item.bookId)
        assertTrue(item.isLiked)
    }

    @Test
    fun firebaseBookService_updateLibraryLiked_verifyFailure_hasLibraryLiked()= runTest{
        val fakeFirebaseBookService= FirebaseBookService(FakeExceptionBookRepository(), FakeTimeProvider())

        assertFailsWith<CheckLibraryLikeFailedException>{
            fakeFirebaseBookService.updateLibraryLiked("", "",true)
        }
    }

}