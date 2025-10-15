package com.example.library.data.repository

import com.example.library.data.entity.Item
import com.example.library.domain.RemoteRepository
import com.example.library.data.api.VolumesApiService
import com.example.library.data.mapper.toItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkBookRepository @Inject constructor(
    private val volumesApiService: VolumesApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RemoteRepository {
    override suspend fun searchVolume(query:String, limit:Int, offset:Int): Result<Item> =
        withContext(ioDispatcher){
            return@withContext try{
                val item= volumesApiService.searchVolume(query, limit, offset).toItem()
                Result.success(item)
            }catch (e:Exception){
                Result.failure(e)
            }
        }
}