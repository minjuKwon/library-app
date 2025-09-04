package com.example.library.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.library.data.entity.Book
import com.example.library.domain.BookRepository

class CacheLibraryPagingSource(
    private val bookRepository: BookRepository,
    private val keyword:String,
    private val pageSize:Int,
    private val pageNumber:Int
): PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try{
            val offset = pageSize * (pageNumber-1)
            val data=bookRepository
                .searchVolume(
                    query=keyword,
                    limit= pageSize,
                    offset = offset
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