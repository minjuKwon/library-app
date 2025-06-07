package com.example.library.ui.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class LibraryDestination(val route:String){
    object Books: LibraryDestination("books")
    object Details: LibraryDestination("details"){
        const val BOOK_ID_ARGS="book_id"
        val routeWithArgs="$route/{$BOOK_ID_ARGS}"
        val arguments = listOf(
            navArgument(BOOK_ID_ARGS){type= NavType.StringType}
        )
    }
    object Ranking: LibraryDestination("ranking")
    object Setting: LibraryDestination(SettingRoutes.ROOT)
    object UserEdit: LibraryDestination(SettingRoutes.EDIT)
    object LoanHistory: LibraryDestination(SettingRoutes.LOAN_HISTORY)
    object LoanStatus: LibraryDestination(SettingRoutes.LOAN_STATUS)
    object ReservationStatus: LibraryDestination(SettingRoutes.RESERVATION_STATUS)
    object Auth: LibraryDestination(AuthRoutes.ROOT)
    object LogIn: LibraryDestination(AuthRoutes.LOG_IN)
    object Register: LibraryDestination(AuthRoutes.REGISTER)
}

object SettingRoutes{
    const val ROOT="setting"
    const val EDIT="$ROOT/edit"
    const val LOAN_HISTORY ="$ROOT/loan_history"
    const val LOAN_STATUS= "$ROOT/loan_status"
    const val RESERVATION_STATUS= "$ROOT/reservation_status"
}

object AuthRoutes{
    const val ROOT="auth"
    const val LOG_IN="$ROOT/log_in"
    const val REGISTER="$ROOT/register"
}

object GraphRoutes{
    const val HOME="home"
}