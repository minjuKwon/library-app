package com.example.library.data

object FireStoreCollections {
    const val USER_COLLECTION="users"
    const val SEARCH_RESULTS_COLLECTION = "search_results"
    const val PAGE_NUMBER_COLLECTION = "page_number"
    const val LIBRARY_COLLECTION= "library"
    const val LIBRARY_LIKED="library_liked"
    const val LIBRARY_HISTORY="library_history"
}

object FireStoreField{
    const val OFFSET="offset"
    const val USER_ID="userId"
    const val BOOK_ID="bookId"
    const val IS_LIKED="isLiked"
    const val TIMESTAMP="timestamp"
    const val STATUS_TYPE="statusType"
    const val BORROWED_AT="borrowedAt"
    const val DUE_DATE="dueDate"
}