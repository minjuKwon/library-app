package com.example.library.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName(value="items")
    val book:List<VolumeDto>,
    @SerialName(value="totalItems")
    val totalCount:Int
)

@Serializable
data class VolumeDto(
   val id:String,
   @SerialName(value="volumeInfo")
   val bookInfo: VolumeInfoDto
)

@Serializable
data class VolumeInfoDto(
    val title:String?=null,
    val authors:List<String>?=null,
    val publisher:String?=null,
    val publishedDate:String?=null,
    val description:String?=null,
    @SerialName(value="imageLinks")
    val img: VolumeImageDto?=null
)

@Serializable
data class VolumeImageDto(
    val thumbnail:String?=null,
    val small:String?=null,
    val medium:String?=null,
    val large:String?=null,
    val smallThumbnail:String?=null,
)