package com.example.library.ui.screens.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.library.R
import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.ui.common.LibraryListItem
import com.example.library.ui.common.LibraryUiModel
import com.example.library.ui.common.TextRadioButton
import com.example.library.ui.common.ListContentParams

@Composable
fun LibraryRankingScreen(
    listContentParams:ListContentParams
){
    val tempBook =Book("1",
    BookInfo("android_1",listOf("1_1","1_2"),"publisher1",
        "0101","description1", BookImage(),false)
    )
    val tempLibrary= Library("1",tempBook, BookStatus.Available, "123.f","3층",0)
    Column(
        modifier= Modifier
            .padding(
                horizontal=dimensionResource(R.dimen.padding_lg),
                vertical=dimensionResource(R.dimen.padding_xl)
            )
    ){
        TextRadioButton(
            listOf(
                stringResource(R.string.most_borrowed),
                stringResource(R.string.most_liked)
            )
        )
        DropDownDateMenu()
        LazyColumn{
            for(idx in 1..3){
                item{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Text(
                            text="$idx",
                            modifier=Modifier.padding(end= dimensionResource(R.dimen.padding_xl))
                        )
                        LibraryListItem(
                            libraryUiModel= LibraryUiModel(tempLibrary, false),
                            onLikedPressed= listContentParams.onLikedPressed,
                            onBookItemPressed=listContentParams.onBookItemPressed,
                            onNavigateToDetails={},
                            onNavigationToLogIn={},
                            isShowLibraryInfo=false
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun DropDownDateMenu(){
    var expanded by remember{mutableStateOf(false)}
    val menuItemData=List(12){"${it+1}"}
    var selectedMenu by remember{ mutableStateOf(menuItemData[0])}
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        IconButton(
            onClick={expanded= true}
        ){
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.ranking_options)
            )
        }
        DropdownMenu(
            expanded= expanded,
            onDismissRequest = {expanded=false}
        ) {
            menuItemData.forEach{ option ->
                DropdownMenuItem(
                    text={Text(option)},
                    onClick={
                        selectedMenu=option
                        expanded=false
                    }
                )
            }
        }
        Text(stringResource(R.string.month, selectedMenu))
    }
}