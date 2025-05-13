package com.example.library.ui.screens.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
                onClick = {detailsScreenParams.onBackPressed(data)}
            ) {
                Icon(
                    imageVector= Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier= Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(dimensionResource(R.dimen.padding_xs))
                        .testTag(stringResource(R.string.test_back))
                )
            }
        }
        Spacer(modifier= Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.detail_screen_spacer_height))
        )
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
        modifier=Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier=Modifier
                .padding(dimensionResource(R.dimen.padding_md))
        ){
            book.img?.let {
                AsyncImage(
                    model=ImageRequest.Builder(context=LocalContext.current)
                        .data(it.thumbnail).build(),
                    error=painterResource(R.drawable.baseline_broken_image_24),
                    placeholder = painterResource(R.drawable.baseline_rotate_left_24),
                    contentDescription = null,
                    modifier= Modifier
                        .height(dimensionResource(R.dimen.detail_screen_image_height))
                )
            }
        }

        DetailScreenContentInformation(book)

        book.description?.let {
            Text(
                text = it,
                style= MaterialTheme.typography.titleSmall,
                modifier= Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(dimensionResource(R.dimen.padding_md))
            )
        }

    }
}

@Composable
private fun DetailScreenContentInformation(
    book: BookInfo
){
    Column(
        modifier=Modifier
            .padding(dimensionResource(R.dimen.padding_xs))
    ) {

        book.title?.let {
            Text(
                text= it,
                style = MaterialTheme.typography.titleLarge,
                modifier= Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(dimensionResource(R.dimen.padding_md))
                    .testTag(it)
            )
        }

        Row(modifier=Modifier.align(Alignment.End)){
            book.authors?.forEach{
                Text(
                    text="$it ",
                    style= MaterialTheme.typography.titleSmall,
                    modifier=Modifier
                        .padding(
                            end=dimensionResource(R.dimen.padding_md)
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
                        end = dimensionResource(R.dimen.padding_md)
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
                        end = dimensionResource(R.dimen.padding_md)
                    )
            )
        }

    }
}