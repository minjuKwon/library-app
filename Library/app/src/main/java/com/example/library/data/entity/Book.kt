package com.example.library.data.entity

data class Item(
    val book:List<Book>,
    val totalCount:Int
)

data class Book(
    val id:String= "",
    val bookInfo: BookInfo= BookInfo()
)

data class BookInfo(
    val title:String?=null,
    val authors:List<String>?=null,
    val publisher:String?=null,
    val publishedDate:String?=null,
    val description:String?=null,
    val img: BookImage?=null
)

data class BookImage(
    val thumbnail:String?=null,
    val small:String?=null,
    val medium:String?=null,
    val large:String?=null,
    val smallThumbnail:String?=null,
)