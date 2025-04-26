package com.example.library.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.library.network.Book

class BookPagingSource(
    private val bookshelfRepository: BookshelfRepository,
    private val keyword:String,
    private val pageSize:Int,
    private val pageNumber:Int
): PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try{
            val limit = pageSize * (pageNumber-1)
            val data=bookshelfRepository
                .getBookListInformation(
                    query=keyword,
                    count=pageSize,
                    startIndex = limit
            ).book
            LoadResult.Page(data=data, prevKey = null, nextKey = null)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return null
    }

}