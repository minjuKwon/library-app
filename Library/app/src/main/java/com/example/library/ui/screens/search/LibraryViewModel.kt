package com.example.library.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.User
import com.example.library.data.mapper.toListUiModel
import com.example.library.di.ApplicationScope
import com.example.library.domain.LibrarySyncService
import com.example.library.domain.SessionManager
import com.example.library.service.FirebaseBookService
import com.example.library.ui.common.LibraryUiModel
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
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

    private val _userPreferences: StateFlow<User?> =
        defaultSessionManager.userPreferences.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
    )
    val userPreferences= _userPreferences

    var libraryUiState: LibraryUiState by mutableStateOf(LibraryUiState.Loading)
        private set

    private val _textFieldKeyword = mutableStateOf("android")
    val textFieldKeyword=_textFieldKeyword

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _backPressedTime= MutableStateFlow(0L)
    private val _loadComplete = MutableStateFlow(false)
    private val likeCountListeners = mutableMapOf<String, ListenerRegistration>()
    private var bookStatusListener:ListenerRegistration? =null

    init {
        getInformation()
        getItem()
    }

    override fun onCleared() {
        super.onCleared()
        likeCountListeners.values.forEach { it.remove() }
    }

    fun getInformation(
        search:String=_textFieldKeyword.value,
        page:Int=1
    ){
        scope.launch {
            _loadComplete.value = false
            libraryUiState= LibraryUiState.Loading

            libraryUiState = try{
                _currentPage.value=page

                val list= librarySyncService.getSearchBooks(search, page)
                val totalItemCount= librarySyncService.getTotalCntForKeyword(search)

                if(list!=null&&totalItemCount!=null) {
                    val uid=awaitUserId()
                    val likedList= firebaseBookService.getLibraryLikedList(uid)

                    if(likedList.isFailure){
                        LibraryUiState.Error
                    }else{
                        val likedResult= likedList.getOrNull()
                        if(likedResult!=null){
                            var uiList= list.toListUiModel()
                            uiList= updateLikedList(likedResult, uiList)
                            _loadComplete.value = true

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

    fun getItem(){
        scope.launch {
            _loadComplete.filter { it }.first()
            getBookStatus()
            getLikedCount()
        }
    }

    fun getBookStatus(){
        bookStatusListener?.remove()

        if(libraryUiState is LibraryUiState.Success){
            val state= libraryUiState as LibraryUiState.Success
            state.list.forEach { item ->
                val bookId = item.library.book.id

                val registration = firebaseBookService.getLibraryStatus(
                    bookId = bookId,
                    callback = { updateBookStatus(bookId, it) }
                )

                bookStatusListener = registration
            }
        }else{
            libraryUiState= LibraryUiState.Error
        }
    }

    private fun updateBookStatus(bookId: String, status: BookStatus) {
        libraryUiState= updateCopiedUiState(libraryUiState){
            val updatedList = it.list.map { item ->
                if (item.library.book.id == bookId)
                    item.copy(library = item.library.copy(bookStatus = status))
                else item
            }
            it.copy(list = updatedList)
        }
    }

    private fun getLikedCount(){
        likeCountListeners.values.forEach { it.remove() }
        likeCountListeners.clear()

        if(libraryUiState is LibraryUiState.Success){
            val state= libraryUiState as LibraryUiState.Success
            state.list.forEach { item ->
                val bookId = item.library.book.id

                val registration = firebaseBookService.getLibraryLikedCount(
                    bookId = bookId,
                    onUpdate = { updateCount(bookId, it) }
                )

                likeCountListeners[bookId] = registration
            }
        }else{
            libraryUiState= LibraryUiState.Error
        }
    }

    private fun updateCount(bookId: String, count: Int) {
        libraryUiState= updateCopiedUiState(libraryUiState){
            val updatedList = it.list.map { item ->
                if (item.library.book.id == bookId)
                    item.copy(count = count)
                else item
            }
            it.copy(list = updatedList)
        }
    }

    fun toggleLike(bookId:String, isLiked:Boolean){
        scope.launch {
            libraryUiState = try{
                LibraryUiState.Loading
                val likedList= firebaseBookService.updateLibraryLiked(
                    awaitUserId(),
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
            val likedList= firebaseBookService.getLibraryLikedList(awaitUserId())
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

    fun resetLiked(){
        scope.launch {
            libraryUiState = updateCopiedUiState(libraryUiState){
                val updatedList= it.list.map { library ->
                    library.copy(isLiked = false)
                }
                it.copy(list=updatedList)
            }
        }
    }

    fun getCurrentBookStatus(bookId:String):BookStatus?{
        return when(libraryUiState){
            is LibraryUiState.Success -> {
                val state= libraryUiState as LibraryUiState.Success
                var bookStatus:BookStatus? =null

                state.list.forEach { item ->
                    if(item.library.book.id==bookId){
                        bookStatus=item.library.bookStatus
                        return@forEach
                    }
                }

                return bookStatus
            }
            else -> null
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

    private suspend fun awaitUserId(): String {
        return userPreferences.filterNotNull().first().uid
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