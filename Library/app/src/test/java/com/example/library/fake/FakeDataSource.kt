package com.example.library.fake

import com.example.library.network.Book
import com.example.library.network.BookInfo
import com.example.library.network.Image
import com.example.library.network.Item

object FakeDataSource {
    val item= Item(
        listOf(
        Book("1",
            BookInfo("android_1",listOf("1_1","1_2"),"publisher1",
                "1111","description1", Image(),false)
        ),
        Book("2",
            BookInfo("android_2",listOf("2_1","2_2"),"publisher2",
                "0202","description2", Image(),false)
        ),
        Book("3",
            BookInfo("android_3",listOf("3_1","3_2"),"publisher3",
                "0303","description3", Image(),false)
        ),
    ),0)

    val itemBookmarked= Item(
        listOf(
        Book("1",
            BookInfo("android_1",listOf("1_1","1_2"),"publisher1",
                "1111","description1", Image(),true)
        ),
        Book("2",
            BookInfo("android_2",listOf("2_1","2_2"),"publisher2",
                "0202","description2", Image(),true)
        ),
        Book("3",
            BookInfo("android_3",listOf("3_1","3_2"),"publisher3",
                "0303","description3", Image(),true)
        ),
    ),0)
}