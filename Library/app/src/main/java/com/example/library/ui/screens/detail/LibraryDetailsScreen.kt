package com.example.library.ui.screens.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.ui.common.BackIconButton
import com.example.library.ui.common.BookStatusUiMapper.toStringName
import com.example.library.ui.common.TextRadioButton
import com.example.library.ui.common.DetailsScreenParams
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun LibraryDetailsScreen(
    isNotFullScreen:Boolean=true,
    detailsScreenParams: DetailsScreenParams,
    onBackPressed:()->Unit,
    modifier: Modifier =Modifier
){
    BackHandler {
        onBackPressed()
    }
    Column(modifier=modifier) {
        if(isNotFullScreen){
            BackIconButton { onBackPressed() }
        }
        LazyColumn(
            modifier=Modifier
                .padding(bottom= dimensionResource(R.dimen.padding_sm))
        ){
            item{
                when(detailsScreenParams.uiState){
                    is LibraryDetailsUiState.Success->{
                        DetailsScreenContent(detailsScreenParams)
                    }
                    is LibraryDetailsUiState.Loading->{
                        Box(
                            modifier=Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                    is LibraryDetailsUiState.Error->{
                        Box(
                            modifier=Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(stringResource(R.string.load_data_error))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsScreenContent(
    detailsScreenParams: DetailsScreenParams
){
    val bookInfo= detailsScreenParams.currentBook.book.bookInfo

    LaunchedEffect(Unit) {
        detailsScreenParams.isSuccessLoan.collect{ success->
            if(success){
                detailsScreenParams.getBookStatus()
                val status=detailsScreenParams.getCurrentBookStatus(detailsScreenParams.currentBook.book.id)
                status?.let { detailsScreenParams.updateCurrentBookStatus(it) }
                detailsScreenParams.resetLoanFlag()
            }
        }
    }

    Column(
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal= dimensionResource(R.dimen.padding_lg)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Card{
                bookInfo.img?.let {
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
            DetailsScreenContentInformation(bookInfo)
        }

        bookInfo.description?.let {
            DetailsScreenContentDescription(it)
        }

        DetailsScreenLibraryInformation(detailsScreenParams)

        BookStatusButton(detailsScreenParams)

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
    var isExpanded by rememberSaveable { mutableStateOf(false) }

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
                overflow = if(isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                maxLines = if(isExpanded) Int.MAX_VALUE else 5,
                style= MaterialTheme.typography.titleSmall,

            )
            Text(
                text= if (isExpanded) stringResource(R.string.see_less) else stringResource(R.string.see_more),
                style=MaterialTheme.typography.labelSmall,
                modifier =Modifier
                    .padding(top= dimensionResource(R.dimen.padding_xs))
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}

@Composable
private fun DetailsScreenLibraryInformation(
    detailsScreenParams: DetailsScreenParams
){
    val library= detailsScreenParams.currentBook

    Card(
        modifier=Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_md))
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
                Text(text=stringResource(R.string.call_number))
                Text(text=library.callNumber)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text=stringResource(R.string.location))
                Text(text=library.location)
            }
            if(library.bookStatus is BookStatus.Borrowed){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text=stringResource(R.string.expected_return_date))
                    val date= library.bookStatus.dueDate
                    val formattedDate= formatDateOnly(date)
                    Text(text= formattedDate)
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text=stringResource(R.string.book_status))
                Text(text=library.bookStatus.toStringName())
            }
        }
    }
}

private fun formatDateOnly(date: Instant): String  {
    val seoulZone = ZoneId.of("Asia/Seoul")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return date
        .atZone(seoulZone)
        .toLocalDate()
        .format(formatter)
}

@Composable
private fun BookStatusButton(
    detailsScreenParams: DetailsScreenParams
){
    Button(
        onClick = {detailsScreenParams.loanLibrary()},
        modifier=Modifier.fillMaxWidth()
    ) {
        when(detailsScreenParams.currentBook.bookStatus){
            is BookStatus.Available -> Text(stringResource(R.string.borrow_book))
            is BookStatus.UnAvailable -> Text("")
            is BookStatus.Borrowed -> Text(stringResource(R.string.return_book))
            is BookStatus.Reserved -> Text("")
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
        TextRadioButton(
            listOf(
            stringResource(R.string.most_relatable),
            stringResource(R.string.newest)
            )
        )
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
                Text(text=stringResource(R.string.see_more))
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