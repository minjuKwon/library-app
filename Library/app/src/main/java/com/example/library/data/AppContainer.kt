package com.example.library.data

import com.example.library.data.api.VolumesApiService
import com.example.library.data.repository.NetworkBookRepository
import com.example.library.domain.BookRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookRepository: BookRepository
}

class DefaultAppContainer: AppContainer{

    private val baseUrl=
        "https://www.googleapis.com/books/v1/"

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(contentType))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : VolumesApiService by lazy {
        retrofit.create(VolumesApiService::class.java)
    }

    override val bookRepository: BookRepository by lazy{
        NetworkBookRepository(retrofitService)
    }

}