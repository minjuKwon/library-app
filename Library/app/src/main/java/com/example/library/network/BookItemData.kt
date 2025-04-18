package com.example.library.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName(value="items")
    val book:List<Book>,
    @SerialName(value="totalItems")
    val totalCount:Int
)

@Serializable
data class Book(
   val id:String,
   @SerialName(value="volumeInfo")
   val bookInfo: BookInfo
)

@Serializable
data class BookInfo(
    val title:String?=null,
    val authors:List<String>?=null,
    val publisher:String?=null,
    val publishedDate:String?=null,
    val description:String?=null,
    @SerialName(value="imageLinks")
    val img:Image?=null,
    var isBookmarked:Boolean=false
)

@Serializable
data class Image(
    val thumbnail:String?=null,
    val small:String?=null,
    val medium:String?=null,
    val large:String?=null,
    val smallThumbnail:String?=null,
)