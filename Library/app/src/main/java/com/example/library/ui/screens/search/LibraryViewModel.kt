package com.example.library.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.User
import com.example.library.data.mapper.toListUiModel
import com.example.library.di.ApplicationScope
import com.example.library.domain.LibrarySyncService
import com.example.library.domain.SessionManager
import com.example.library.service.FirebaseBookService
import com.example.library.ui.common.LibraryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val librarySyncService: LibrarySyncService,
    private val firebaseBookService: FirebaseBookService,
    defaultSessionManager: SessionManager,
    @ApplicationScope externalScope: CoroutineScope? = null
):ViewModel() {

    private val scope = externalScope ?: viewModelScope

    private val _userPreferences: StateFlow<User> =
        defaultSessionManager.userPreferences.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            User()
    )
    val userPreferences= _userPreferences

    var libraryUiState: LibraryUiState by mutableStateOf(LibraryUiState.Loading)
        private set

    private val _textFieldKeyword = mutableStateOf("android")
    val textFieldKeyword=_textFieldKeyword

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _backPressedTime= MutableStateFlow(0L)

    init {
        getInformation()
    }

    fun getInformation(
        search:String=_textFieldKeyword.value,
        page:Int=1
    ){
        scope.launch {
            libraryUiState= LibraryUiState.Loading

            libraryUiState = try{
                _currentPage.value=page

                val list= librarySyncService.getSearchBooks(search, page)
                val totalItemCount= librarySyncService.getTotalCntForKeyword(search)

                if(list!=null&&totalItemCount!=null) {
                    var uiList= list.toListUiModel()
                    val likedList= firebaseBookService.getLibraryLiked(_userPreferences.value.uid)
                    if(likedList.isFailure){
                        LibraryUiState.Error
                    }else{
                        val likedResult= likedList.getOrNull()
                        if(likedResult!=null){
                            uiList= updateLikedList(likedResult, uiList)
                            LibraryUiState.Success(totalItemCount,uiList)
                        }else{
                            LibraryUiState.Error
                        }
                    }
                }else{
                    LibraryUiState.Success(0, emptyList())
                }
            }catch (e: Exception){
                LibraryUiState.Error
            }
        }
    }

    fun toggleLike(bookId:String, isLiked:Boolean){
        scope.launch {
            libraryUiState = try{
                LibraryUiState.Loading
                val likedList= firebaseBookService.updateLibraryLiked(
                    _userPreferences.value.uid,
                    bookId,
                    isLiked
                )

                if(likedList.isFailure){
                    LibraryUiState.Error
                } else{
                    val likedResult= likedList.getOrNull()
                    if(likedResult!=null){
                        updateCopiedUiState(libraryUiState){
                            val list= updateLikedList(likedResult, it.list)
                            it.copy(list=list)
                        }
                    }else{
                        LibraryUiState.Error
                    }
                }
            }catch (e: Exception){
                LibraryUiState.Error
            }

        }
    }

    fun getLiked(){
        scope.launch {
            val likedList= firebaseBookService.getLibraryLiked(_userPreferences.value.uid)
            if(likedList.isFailure){
                LibraryUiState.Error
            }else{
                libraryUiState = updateCopiedUiState(libraryUiState){
                    var uiList= it.list
                    val likedResult= likedList.getOrNull()
                    if(likedResult!=null){
                        uiList= updateLikedList(likedResult, uiList)
                    }else{
                        LibraryUiState.Error
                    }
                    it.copy(list=uiList)
                }
            }
        }
    }
    fun updateKeyword(input:String){
        _textFieldKeyword.value=input
    }

    fun updateBackPressedTime(currentTime:Long){
        _backPressedTime.value= currentTime
    }

    fun isBackPressedDouble():Boolean{
        //백 스택의 루트일 때(첫 화면), 뒤로가기 버튼을 연속 두번 누르면(2000미만) 앱 종료
        val currentTime= System.currentTimeMillis()
        return currentTime - _backPressedTime.value<2000
    }

    private fun updateLikedList(
        likedList:List<LibraryLiked>,
        uiList:List<LibraryUiModel>
    ):List<LibraryUiModel>{
        val likedMap= likedList.associateBy { it.bookId }

        val updatedList = uiList.map { library ->
            likedMap[library.library.book.id]?.let {
                library.copy(isLiked = it.isLiked)
            }?:library
        }

        return updatedList
    }

    private fun updateCopiedUiState(
        uiState: LibraryUiState,
        copyOperation:(LibraryUiState.Success)-> LibraryUiState.Success
    ): LibraryUiState
    {
        return when(uiState){
            is LibraryUiState.Success -> copyOperation(uiState)
            else ->uiState
        }
    }

}