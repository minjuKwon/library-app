package com.example.library.data.mapper

import com.example.library.data.api.ItemDto
import com.example.library.data.api.VolumeDto
import com.example.library.data.api.VolumeImageDto
import com.example.library.data.api.VolumeInfoDto
import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.Item

fun ItemDto.toItem(): Item = Item(
    book = this.book.map { it.toBook() },
    totalCount = this.totalCount
)

fun VolumeDto.toBook(): Book = Book(
    id = this.id,
    bookInfo = this.bookInfo.toBookInfo()
)

fun VolumeInfoDto.toBookInfo() = BookInfo(
    title= this.title,
    authors = this.authors,
    publisher = this.publisher,
    publishedDate= this.publishedDate,
    description = this.description,
    img = this.img?.toBookImage()
)

fun VolumeImageDto.toBookImage() = BookImage(
    smallThumbnail = this.smallThumbnail,
    thumbnail = this.thumbnail,
    small= this.small,
    medium = this.medium,
    large = this.large
)