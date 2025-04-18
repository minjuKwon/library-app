package com.example.library.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.library.network.Book

class BookPagingSource(
    private val bookshelfRepository: BookshelfRepository,
    private val input:String,
    private val pageSize:Int,
    private val page:Int
): PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try{
            val offset = pageSize * (page-1)
            val data=bookshelfRepository
                .getBookListInformation(
                    query=input,
                    count=pageSize,
                    startIndex = offset
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