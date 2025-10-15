package com.example.library.service

import com.example.library.data.entity.Library
import com.example.library.data.transformToLibrary
import com.example.library.domain.GetSearchBooksCommands
import com.example.library.domain.LibrarySyncService
import com.example.library.domain.RemoteRepository
import javax.inject.Inject

class DefaultLibrarySyncService @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val cacheBookService: CacheBookService,
    private val firebaseBookService: FirebaseBookService
): LibrarySyncService {

    override suspend fun getSearchBooks(getSearchBooksCommands: GetSearchBooksCommands): List<Library>? {
        val strPage= getSearchBooksCommands.pageNumber.toString()

        val isCached= cacheBookService.isKeywordCached(
            getSearchBooksCommands.keyword,
            getSearchBooksCommands.pageNumber
        )

        //캐시된 데이터가 있으면 가져오기
        if(isCached){
         return cacheBookService.getLibraryBooks(
             getSearchBooksCommands.keyword,
             getSearchBooksCommands.offset,
             getSearchBooksCommands.pageNumber
         )
        }else{
            val isSavedFirebase= firebaseBookService.isSavedBook(getSearchBooksCommands.keyword, strPage)
            if(isSavedFirebase.isFailure) throw isSavedFirebase.exceptionOrNull()?:SaveLibraryInfoFailedException()

            val firebaseResult= isSavedFirebase.getOrNull()
            firebaseResult?.let {
                //캐시된 데이터가 없지만 firebase 저장된 데이터가 있으면 가져오고 캐시하기
                if(firebaseResult){
                    val isGetList= firebaseBookService
                        .getLibraryBooks(getSearchBooksCommands.keyword, strPage)
                    if(isGetList.isFailure) throw isGetList.exceptionOrNull()?:GetLibraryInfoFailedException()

                    val firebaseList= isGetList.getOrNull()
                    if(firebaseList!=null){
                        firebaseList.forEach {
                            cacheBookService.saveLibraryBooks(
                                it,
                                getSearchBooksCommands.keyword,
                                getSearchBooksCommands.pageNumber,
                                getSearchBooksCommands.cachedAt,
                                getSearchBooksCommands.accessedAt
                            )
                        }
                        return firebaseList
                    }
                }else{
                    //원천 book 데이터를 가져와서 library 정보 생성 후 firebase, room에 저장
                    val isGetItem= remoteRepository.searchVolume(
                        getSearchBooksCommands.keyword,
                        getSearchBooksCommands.limit,
                        getSearchBooksCommands.offset
                    )
                    if(isGetItem.isFailure) isGetItem.exceptionOrNull()?:GetBookFailedException()

                    val sourceResult= isGetItem.getOrNull()
                    if(sourceResult!==null){
                        val sourceList= sourceResult.book.map{
                            it.transformToLibrary(
                                getSearchBooksCommands.keyword,
                                getSearchBooksCommands.offset
                            )
                        }
                        val isSaved= firebaseBookService
                            .saveLibraryBooks(getSearchBooksCommands.keyword, strPage, sourceList)
                        if(isSaved.isFailure)
                            isSaved.exceptionOrNull()?:SaveLibraryInfoFailedException()

                        sourceList.forEach {
                            cacheBookService.saveLibraryBooks(
                                it,
                                getSearchBooksCommands.keyword,
                                getSearchBooksCommands.pageNumber,
                                getSearchBooksCommands.cachedAt,
                                getSearchBooksCommands.accessedAt
                            )
                        }
                        return sourceList
                    }
                }
            }
        }
        return null
    }

    override suspend fun getTotalCntForKeyword(keyword: String): Int? {
        var count = cacheBookService.getTotalCountForKeyword(keyword)
        if(count==null){
            val isGetItem= remoteRepository.searchVolume(keyword,10,0)
            if(isGetItem.isFailure) isGetItem.exceptionOrNull()?:GetBookFailedException()

            val sourceResult= isGetItem.getOrNull()
            if(sourceResult!=null){
                count= sourceResult.totalCount
                cacheBookService.saveTotalCount(keyword, count)
            }
        }
        return count
    }

}