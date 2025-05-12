package com.example.library.data.fake

import androidx.compose.ui.res.stringResource
import com.example.library.R
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import com.example.library.data.api.Image
import com.example.library.data.api.Item
import kotlin.math.min

object FakeDataSource {
    private val item= Item(
        listOf(
            Book("1",
                BookInfo("android_1",listOf("1_1","1_2"),"publisher1",
                    "0101","description1", Image(),false)
            ),
            Book("2",
                BookInfo("android_2",listOf("2_1","2_2"),"publisher2",
                    "0202","description2", Image(),false)
            ),
            Book("3",
                BookInfo("android_3",listOf("3_1","3_2"),"publisher3",
                    "0303","description3", Image(),false)
            ),
            Book("4",
                BookInfo("android_4",listOf("4_1","4_2"),"publisher4",
                    "0404","description4", Image(),false)
            ),
            Book("5",
                BookInfo("android_5",listOf("5_1","5_2"),"publisher5",
                    "0505","description5", Image(),false)
            ),
            Book("6",
                BookInfo("android_6",listOf("6_1","6_2"),"publisher6",
                    "0606","description6", Image(),false)
            ),
            Book("7",
                BookInfo("android_7",listOf("7_1","7_2"),"publisher7",
                    "0707","description7", Image(),false)
            ),
            Book("8",
                BookInfo("android_8",listOf("8_1","8_2"),"publisher8",
                    "0808","description8", Image(),false)
            ),
            Book("9",
                BookInfo("android_9",listOf("9_1","9_2"),"publisher9",
                    "0909","description9", Image(),false)
            ),
            Book("10",
                BookInfo("android_10",listOf("10_1","10_2"),"publisher10",
                    "1010","description10", Image(),false)
            ),
            Book("11",
                BookInfo("android_11",listOf("11_1","11_2"),"publisher11",
                    "1111","description11", Image(),false)
            ),
            Book("12",
                BookInfo("android_12",listOf("12_1","12_2"),"publisher12",
                    "1212","description12", Image(),false)
            ),
            Book("13",
                BookInfo("android_13",listOf("13_1","13_2"),"publisher3",
                    "1313","description13", Image(),false)
            ),
            Book("14",
                BookInfo("android_14",listOf("14_1","14_2"),"publisher14",
                    "1414","description14", Image(),false)
            ),
            Book("15",
                BookInfo("android_15",listOf("15_1","15_2"),"publisher15",
                    "1515","description15", Image(),false)
            )
    ),15)

    fun getListRange(limit:Int, offset:Int): Item {
        return Item(item.book.subList(offset, min(offset+limit, item.totalCount)), item.totalCount)
    }
}