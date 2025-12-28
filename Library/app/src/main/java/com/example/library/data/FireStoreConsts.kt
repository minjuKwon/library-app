package com.example.library.data

object FireStoreCollections {
    const val USER_COLLECTION="users"
    const val USER_LOAN_LIBRARY_COLLECTION="user_loan_library"
    const val SEARCH_RESULTS_COLLECTION = "search_results"
    const val PAGE_NUMBER_COLLECTION = "page_number"
    const val LIBRARY_COLLECTION= "library"
    const val LIBRARY_LIKED_COLLECTION="library_liked"
    const val LIBRARY_HISTORY_COLLECTION="library_history"
    const val LIBRARY_RESERVATION_COLLECTION="library_reservation"
}

object FireStoreField{
    const val OFFSET="offset"
    const val USER_ID="userId"
    const val BOOK_ID="bookId"
    const val BOOK__ID="book.id"
    const val IS_LIKED="isLiked"
    const val TIMESTAMP="timestamp"
    const val STATUS="status"
    const val STATUS_TYPE="statusType"
    const val BORROWED_AT="borrowedAt"
    const val RESERVED_AT="reservedAt"
    const val LOAN_DATE="loanDate"
    const val DUE_DATE="dueDate"
    const val RETURN_DATE="returnDate"
    const val OVERDUE_DATE="overdueDate"
}