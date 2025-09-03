package com.example.library.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface VolumesApiService {
    @GET("volumes")
    suspend fun searchVolume(
        @Query("q")query:String,
        @Query("maxResults")limit:Int,
        @Query("startIndex")offset:Int
    ): ItemDto
}
