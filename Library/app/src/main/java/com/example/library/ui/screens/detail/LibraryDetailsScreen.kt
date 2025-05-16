package com.example.library.ui.screens.detail

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.ui.screens.search.getCurrentItem
import com.example.library.data.api.BookInfo
import com.example.library.ui.BackIconButton
import com.example.library.ui.TextRadioButton
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
            BackIconButton { detailsScreenParams.onBackPressed(data) }
        }
        LazyColumn(
            modifier=Modifier
                .padding(bottom= dimensionResource(R.dimen.padding_sm))
        ){
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
            DetailsScreenContentInformation(book)
        }

        book.description?.let {
            DetailsScreenContentDescription(it)
        }

        DetailsScreenLibraryInformation()

        DetailsScreenComment()
    }
}

@Composable
private fun DetailsScreenContentInformation(
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
private fun DetailsScreenContentDescription(
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

@Composable
private fun DetailsScreenLibraryInformation(){
    Card(
        modifier=Modifier
            .fillMaxWidth()
            .padding(top= dimensionResource(R.dimen.padding_md))
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier=Modifier
                .fillMaxWidth()
                .padding(
                    horizontal=dimensionResource(R.dimen.padding_xl),
                    vertical= dimensionResource(R.dimen.padding_md)
                )
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text="청구기호")
                Text(text="abcd.123")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text="위치")
                Text(text="3층 인문학실")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text="대출상태")
                Text(text="대출가능")
            }
        }
    }
}

@Composable
private fun DetailsScreenComment(){
    Column{
        CommentTextField()
        CommentInformation()
        for(i in 0..5){
            CommentListItem()
        }
    }
}

@Composable
private fun CommentTextField(){
    OutlinedTextField(
        value = "",
        onValueChange = {},
        trailingIcon={
            Icon(
                imageVector = Icons.Filled.ArrowCircleUp,
                contentDescription = stringResource(R.string.comment_upload),
                modifier= Modifier
                    .clickable {  }
                    .padding(
                        dimensionResource(R.dimen.padding_md)
                    )
            )
        },
        keyboardOptions= KeyboardOptions.Default.copy(
            imeAction= ImeAction.Done
        ),
        keyboardActions= KeyboardActions(
            onDone = {},
        ),
        modifier= Modifier
            .fillMaxWidth()
            .padding(
                top =dimensionResource(R.dimen.padding_lg)
            )
    )
}

@Composable
private fun CommentInformation(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .padding(
                top= dimensionResource(R.dimen.padding_xl),
                start= dimensionResource(R.dimen.padding_xs),
                end= dimensionResource(R.dimen.padding_xs)
            )
            .fillMaxWidth()
    ){
        Text("123")
        TextRadioButton(listOf("공감순","최신순"))
    }
}

@Composable
private fun CommentListItem(){
    OutlinedCard(
        modifier=Modifier
            .fillMaxWidth()
            .padding(top= dimensionResource(R.dimen.padding_md))
    ) {
        Column(
            modifier=Modifier
                .padding(dimensionResource(R.dimen.padding_sm))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier=Modifier.fillMaxWidth()
            ){
                Text(
                    text="작성자",
                    modifier=Modifier
                        .padding(end=dimensionResource(R.dimen.padding_sm))
                )
                Text("2025-00-00")
            }
            Text(
                text="댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용댓글내용",
                modifier=Modifier
                    .padding(top=dimensionResource(R.dimen.padding_sm))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier=Modifier.fillMaxWidth()
            ){
                Text(text="더보기")
                Spacer(modifier=Modifier.weight(1f))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = if(false){Icons.Default.ThumbUp}
                        else {Icons.Default.ThumbUpOffAlt},
                        contentDescription = stringResource(R.string.thumbUp_comment)
                    )
                }
                Text("24")
            }
        }
    }
}