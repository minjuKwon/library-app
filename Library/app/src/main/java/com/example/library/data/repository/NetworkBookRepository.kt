package com.example.library.data.repository

import com.example.library.data.entity.Item
import com.example.library.domain.RemoteRepository
import com.example.library.data.api.VolumesApiService
import com.example.library.data.mapper.toItem
import javax.inject.Inject

class NetworkBookRepository @Inject constructor(
    private val volumesApiService: VolumesApiService
): RemoteRepository {
    override suspend fun searchVolume(query:String, limit:Int, offset:Int): Result<Item> {
        try{
            val item= volumesApiService.searchVolume(query, limit, offset).toItem()
            return Result.success(item)
        }catch (e:Exception){
            return Result.failure(e)
        }
    }
}