package com.example.library.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BookshelfApiService {
    
    @GET("volumes")
    suspend fun getInformation(
        @Query("q")query:String,
        @Query("maxResults")count:Int,
        @Query("startIndex")startIndex:Int
    ):Item

}
