package com.example.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.data.api.Book
import com.example.library.ui.screens.user.UserUiState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun LibraryListItem(
    book: Book,
    onBookMarkPressed:(Book)->Unit,
    onBookItemPressed:(Book)->Unit,
    onNavigateToDetails:(String)->Unit,
    isShowLibraryInfo:Boolean=true,
    isNotFullScreen:Boolean=true
){
    OutlinedCard(
        modifier= Modifier
            .padding(bottom= dimensionResource(R.dimen.padding_lg))
    ){
        Row(
            modifier= Modifier
                .clickable {
                    onBookItemPressed(book)
                    if(isNotFullScreen) onNavigateToDetails(book.id)
                }
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_md)) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier= Modifier
                    .height(dimensionResource(R.dimen.list_item_image_height))
                    .width(dimensionResource(R.dimen.list_item_image_width))
                    .padding(dimensionResource(R.dimen.padding_sm))
            ){
                book.bookInfo.img?.let {
                    AsyncImage(
                        model= ImageRequest.Builder(context= LocalContext.current)
                            .data(it.thumbnail).build(),
                        error= painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = null,
                        contentScale= ContentScale.FillBounds,
                        modifier= Modifier.fillMaxSize()
                    )
                }
            }

            Column(
                modifier= Modifier.padding(horizontal = dimensionResource(R.dimen.padding_lg))
            ) {
                ItemBookDescription(book)
                if(isShowLibraryInfo) ItemLibraryDescription(book, onBookMarkPressed)
            }
        }
    }
}

@Composable
fun ItemBookDescription(
    book: Book
){
    book.bookInfo.title?.let {
        Text(
            text= it,
            style = MaterialTheme.typography.bodyLarge,
            modifier= Modifier
                .padding(bottom= dimensionResource(R.dimen.padding_xs))
                .testTag(it)
        )
    }
    Row(modifier= Modifier
        .wrapContentHeight()
        .fillMaxWidth(0.9f)){
        book.bookInfo.authors?.joinToString(separator = ",")?.let{
            Text(
                text= it,
                maxLines=1,
                style= MaterialTheme.typography.bodySmall,
                modifier= Modifier.padding(
                    bottom= dimensionResource(R.dimen.padding_xs)
                )
            )
        }
    }
    book.bookInfo.publisher?.let {
        Text(
            text= it,
            style= MaterialTheme.typography.bodySmall,
            modifier= Modifier.padding(
                bottom= dimensionResource(R.dimen.padding_xs)
            )
        )
    }
    book.bookInfo.publishedDate?.let {
        Text(
            text= it,
            style= MaterialTheme.typography.bodySmall,
            modifier= Modifier.padding(
                bottom= dimensionResource(R.dimen.padding_xs)
            )
        )
    }
}

@Composable
fun ItemLibraryDescription(
    book: Book,
    onBookMarkPressed:(Book)->Unit
){
    Text(
        text= "abc.13",
        style= MaterialTheme.typography.bodySmall
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier= Modifier.fillMaxWidth(0.8f)
    ){
        Text(
            text= stringResource(R.string.available),
            style= MaterialTheme.typography.bodySmall
        )
        Spacer(modifier= Modifier.weight(1f))
        IconButton(
            onClick = {onBookMarkPressed(book)}
        ) {
            Icon(
                imageVector = if(book.bookInfo.isBookmarked){
                    Icons.Default.Favorite}
                else {
                    Icons.Default.FavoriteBorder},
                contentDescription = stringResource(R.string.liked),
            )
        }
        Spacer(modifier= Modifier.weight(1f))
    }
}

@Composable
fun BackIconButton(
    text:String? =null,
    onClick:()->Unit
){
    Column{
        IconButton(
            onClick = onClick,
            modifier=Modifier.padding(
                horizontal=dimensionResource(R.dimen.padding_lg),
                vertical= dimensionResource(R.dimen.padding_xs)
            )
        ) {
            Icon(
                imageVector= Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier= Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .testTag(stringResource(R.string.test_back))
            )
        }
        text?.let {
            Text(
                text= text,
                modifier=Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_xxl)
                )
            )
        }
    }
}

@Composable
fun TextRadioButton(
    options:List<String>,
    onSexChange:(String)->Unit={}
){
    var selectedOption by remember{ mutableStateOf(options[0]) }
    onSexChange(selectedOption)
    Row {
        options.forEach { option->
            Spacer(modifier=Modifier.width(dimensionResource(R.dimen.padding_lg)))
            Text(
                text=option,
                color = if(selectedOption==option) Color.Black
                else Color.LightGray,
                modifier=Modifier
                    .clickable { selectedOption= option }
            )
        }
    }
}

@Composable
fun Divider(){
    Spacer(
        Modifier
            .height(dimensionResource(R.dimen.user_screen_spacer_height))
            .fillMaxWidth()
            .padding(horizontal=dimensionResource(R.dimen.padding_sm))
            .background(Color.Black)
    )
}

@Composable
fun HandleUserUiState(
    event: SharedFlow<UserUiState>,
    onSuccess:()->Unit,
    onFailure:(UserUiState.Failure)->Unit
){
    LaunchedEffect(Unit){
        event.collect{
            when(it){
                is UserUiState.Success->{
                    onSuccess()
                }
                is UserUiState.Failure -> {
                    onFailure(it)
                }
            }
        }
    }
}