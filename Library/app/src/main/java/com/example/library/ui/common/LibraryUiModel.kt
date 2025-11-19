package com.example.library.ui.common

import com.example.library.data.entity.Library

data class LibraryUiModel(
    val library: Library,
    val isLiked: Boolean,
    val count:Int
)
