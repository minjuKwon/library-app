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
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import com.example.library.data.api.Image
import com.example.library.ui.LibraryListItem
import com.example.library.ui.TextRadioButton

@Composable
fun LibraryRankingScreen(){
    val temp=Book("1",
        BookInfo("android_1",listOf("1_1","1_2"),"publisher1",
            "0101","description1", Image(),false)
    )
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
                        LibraryListItem(temp,{},{},false)
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