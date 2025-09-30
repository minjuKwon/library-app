package com.example.library.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="library")
data class LibraryEntity(
    @PrimaryKey
    val libraryId:String,
    val statusType: String,
    val userEmail: String?,
    val borrowedAt: Long?,
    val reservedAt: Long?,
    val callNumber:String,
    val location:String
)

@Entity(
    tableName = "book",
    foreignKeys = [
        ForeignKey(
            entity= LibraryEntity::class,
            parentColumns = ["libraryId"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices= [Index("id")]
)
data class BookEntity(
    @PrimaryKey
    val id:String,
    val title:String?=null,
    val authors:List<String>?=null,
    val publisher:String?=null,
    val publishedDate:String?=null,
    val description:String?=null
)

@Entity(
    tableName = "book_image",
    foreignKeys = [
        ForeignKey(
            entity= BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices= [Index("id")]
)
data class BookImageEntity(
    @PrimaryKey
    val id:String,
    val thumbnail:String?=null,
    val small:String?=null,
    val medium:String?=null,
    val large:String?=null,
    val smallThumbnail:String?=null
)

@Entity(
    tableName = "search_result",
    primaryKeys = ["libraryId","query"],
    foreignKeys = [
        ForeignKey(
            entity= LibraryEntity::class,
            parentColumns = ["libraryId"],
            childColumns = ["libraryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices= [Index("libraryId")]
)
data class SearchResultEntity(
    val libraryId:String,
    val query:String,
    val page:Int,
    val offset:Int,
    val cachedAt:Long,
    val accessedAt:Long
)

@Entity("search_total_count")
data class SearchTotalCountEntity(
    @PrimaryKey
    val query: String,
    val count:Int
)
