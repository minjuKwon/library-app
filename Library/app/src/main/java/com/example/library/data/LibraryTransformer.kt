package com.example.library.data

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import java.util.Locale
import kotlin.random.Random

fun Book.transformToLibrary(offset:Int): Library = Library(
    book= this,
    bookStatus = BookStatus.Available,
    callNumber = generateCallNumber(),
    location = generateLocation(),
    offset = offset
)

fun generateCallNumber():String{
    val number1= Random.nextInt(0,1000)
    val numberStr1= String.format(Locale.ROOT,"%03d", number1)

    val letter = ('a'..'z').random()

    val number2= Random.nextInt(0,100)
    val numberStr2= String.format(Locale.ROOT,"%02d", number2)

    return "${numberStr1}-${letter}${numberStr2}"
}

fun generateLocation():String {
    val number = Random.nextInt(1,4)
    return "${number}층"
}
