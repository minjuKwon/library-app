package com.example.library.ui.screens.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.ui.screens.search.getCurrentItem
import com.example.library.data.api.BookInfo
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.utils.DetailsScreenParams

@Composable
fun LibraryDetailsScreen(
    libraryUiState: LibraryUiState,
    isNotFullScreen:Boolean=true,
    detailsScreenParams: DetailsScreenParams,
    modifier: Modifier =Modifier
){
    val data= getCurrentItem(libraryUiState)
    BackHandler {
        detailsScreenParams.onBackPressed(data)
    }
    Column(modifier=modifier) {
        if(isNotFullScreen){
            IconButton(
                onClick = {detailsScreenParams.onBackPressed(data)},
                modifier=Modifier.padding(dimensionResource(R.dimen.padding_lg))
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
        }
        LazyColumn{
            item{
                DetailsScreenContent(getCurrentItem(libraryUiState))
                if (detailsScreenParams.isDataReadyForUi) {
                    DetailsScreenContent(getCurrentItem(libraryUiState))
                    detailsScreenParams.updateDataReadyForUi(false)
                }
            }
        }
    }

}

@Composable
private fun DetailsScreenContent(book: BookInfo){
    Column(
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal= dimensionResource(R.dimen.padding_lg)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Card{
                book.img?.let {
                    AsyncImage(
                        model=ImageRequest.Builder(context=LocalContext.current)
                            .data(it.thumbnail).build(),
                        error=painterResource(R.drawable.baseline_broken_image_24),
                        placeholder = painterResource(R.drawable.baseline_rotate_left_24),
                        contentDescription = null,
                        modifier= Modifier
                            .height(dimensionResource(R.dimen.detail_screen_image_height))
                            .width(dimensionResource(R.dimen.detail_screen_image_width))
                    )
                }
            }
            DetailScreenContentInformation(book)
        }

        book.description?.let {
            DetailScreenContentDescription(it)
        }
    }
}

@Composable
private fun DetailScreenContentInformation(
    book: BookInfo
){
    OutlinedCard(
        modifier=Modifier
            .height(dimensionResource(R.dimen.detail_screen_image_height))
            .padding(start=dimensionResource(R.dimen.padding_md))
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier=Modifier
                .padding(horizontal=dimensionResource(R.dimen.padding_sm))
                .fillMaxSize()
        ) {
            book.title?.let {
                Text(
                    text= it,
                    style = MaterialTheme.typography.titleLarge,
                    modifier= Modifier
                        .align(Alignment.End)
                        .padding(top=dimensionResource(R.dimen.padding_md))
                        .testTag(it)
                )
            }
            Row(modifier=Modifier
                .align(Alignment.End)
                .padding(top=dimensionResource(R.dimen.padding_lg))
            ){
                book.authors?.forEach{
                    Text(
                        text="$it ",
                        style= MaterialTheme.typography.titleSmall,
                        modifier=Modifier
                            .padding(
                                end=dimensionResource(R.dimen.padding_sm)
                            )
                    )
                }
            }
            book.publisher?.let {
                Text(
                    text= it,
                    style= MaterialTheme.typography.titleSmall,
                    modifier= Modifier
                        .align(Alignment.End)
                        .padding(
                            top = dimensionResource(R.dimen.padding_sm)
                        )
                )
            }
            book.publishedDate?.let {
                Text(
                    text= it,
                    style= MaterialTheme.typography.titleSmall,
                    modifier= Modifier
                        .align(Alignment.End)
                        .padding(
                            top = dimensionResource(R.dimen.padding_sm),
                            bottom = dimensionResource(R.dimen.padding_md)
                        )
                )
            }
        }
    }
}

@Composable
private fun DetailScreenContentDescription(
    text:String
){
    OutlinedCard(
        modifier=Modifier
            .fillMaxWidth()
            .padding(top= dimensionResource(R.dimen.padding_lg))
    ) {
        Column(
            modifier=Modifier
                .padding(dimensionResource(R.dimen.padding_md))
        ){
            Text(
                text = text,
                style= MaterialTheme.typography.titleSmall,
            )
            Text(
                text="더보기",
                style=MaterialTheme.typography.labelSmall,
                modifier =Modifier
                    .padding(top= dimensionResource(R.dimen.padding_xs))
            )
        }
    }
}