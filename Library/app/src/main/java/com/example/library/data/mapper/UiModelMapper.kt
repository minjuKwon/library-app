package com.example.library.data.mapper

import com.example.library.data.entity.Library
import com.example.library.ui.common.LibraryUiModel

fun List<Library>.toListUiModel()= map{it.toUiModel()}

fun Library.toUiModel()= LibraryUiModel(
    library= this,
    isLiked= false
)