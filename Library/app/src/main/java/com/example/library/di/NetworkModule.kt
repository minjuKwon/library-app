package com.example.library.di

import com.example.library.BuildConfig
import com.example.library.data.api.VolumesApiService
import com.example.library.data.fake.FakeNetworkBookRepository
import com.example.library.data.repository.NetworkBookRepository
import com.example.library.domain.BookRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit():VolumesApiService{
        val baseUrl=
            "https://www.googleapis.com/books/v1/"
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(baseUrl)
            .build()
            .create(VolumesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkBookRepository(volumesApiService: VolumesApiService): BookRepository {
        return if(BuildConfig.DEBUG){
            FakeNetworkBookRepository()
        }else {
            NetworkBookRepository(volumesApiService)
        }
    }

}