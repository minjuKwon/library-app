package com.example.library.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.screens.user.UserViewModel
import com.example.library.ui.theme.LibraryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize=calculateWindowSizeClass(this)
                    val libraryViewModel: LibraryViewModel by viewModels()
                    val libraryDetailsViewModel: LibraryDetailsViewModel by viewModels()
                    val userViwModel: UserViewModel by viewModels()

                    LibraryApp(
                        windowSize=windowSize.widthSizeClass,
                        libraryViewModel= libraryViewModel,
                        libraryDetailsViewModel = libraryDetailsViewModel,
                        userViewModel = userViwModel
                    )
                }
            }
        }
    }
}