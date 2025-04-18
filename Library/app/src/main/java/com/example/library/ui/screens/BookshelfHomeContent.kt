package com.example.library.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.checkBookmarkList
import com.example.library.checkTabPressed
import com.example.library.data.BookType
import com.example.library.getTotalItemsCount
import com.example.library.network.Book
import com.example.library.network.BookInfo
import com.example.library.ui.BookshelfUiState
import com.example.library.ui.DetailsScreenParams
import com.example.library.ui.ListContentParams
import com.example.library.ui.PAGE_SIZE
import com.example.library.ui.TextFieldParams

@Composable
fun BookshelfListOnlyContent(
    books:LazyPagingItems<Book>,
    bookshelfUiState: BookshelfUiState,
    textFieldParams:TextFieldParams,
    listContentParams:ListContentParams,
    modifier:Modifier= Modifier
){
    Column(
        modifier= modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val totalCount= getTotalItemsCount(bookshelfUiState)
        val pageSize = PAGE_SIZE
        val totalPages = (totalCount+pageSize-1)/pageSize
        val pageGroupSize = 3
        val currentGroup = (listContentParams.currentPage-1)/pageGroupSize

        SearchTextField(
            input = textFieldParams.textFieldKeyword,
            onInputChange = textFieldParams.updateKeyword,
            onSearch = textFieldParams.onSearch,
            onInputReset = textFieldParams.updateKeyword
        )

        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(
                        R.dimen.list_only_content_total_text_top_padding
                    ),
                    start = dimensionResource(
                        R.dimen.list_only_content_total_text_start_padding
                    )
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.totalCount))
            Text(text = " $totalCount")
        }

        LazyColumn(
            state=listContentParams.scrollState,
            modifier= Modifier
                .padding(dimensionResource(R.dimen.list_padding))
        ){
            if(checkTabPressed(bookshelfUiState)==BookType.Bookmark){
                    items(checkBookmarkList(bookshelfUiState),key={it.id}){
                        listContentParams.initCurrentItem(
                            checkTabPressed(bookshelfUiState),
                            checkBookmarkList(bookshelfUiState)[0].bookInfo
                        )
                        BookShelfListItem(
                            book = it,
                            onBookItemPressed= listContentParams.onBookItemPressed,
                            onBookMarkPressed = listContentParams.onBookmarkPressed
                        )
                    }
            }else{
                items(count=books.itemCount){
                    books[it]?.let { it1 ->
                        if(it==0){
                            listContentParams.initCurrentItem(
                                checkTabPressed(bookshelfUiState), it1.bookInfo
                            )
                        }
                        BookShelfListItem(
                            book = it1,
                            onBookItemPressed= listContentParams.onBookItemPressed,
                            onBookMarkPressed =listContentParams.onBookmarkPressed
                        )
                    }
                }
                item{
                    books.apply  {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier=Modifier.fillMaxWidth()
                        ){
                            when{
                                loadState.refresh is LoadState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                loadState.refresh is LoadState.Error ->{
                                    Text(stringResource(R.string.load_data_error))
                                }
                                loadState.append is LoadState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                loadState.append is LoadState.Error -> {
                                    Text(stringResource(R.string.load_data_error))
                                }
                            }
                        }
                    }
                }
                item {
                    PageNumberButton(
                        currentGroup = currentGroup,
                        pageGroupSize = pageGroupSize,
                        totalPages = totalPages,
                        updatePage = listContentParams.updatePage,
                        currentPage = listContentParams.currentPage
                    )
                }
            }
        }

    }
}

@Composable
fun BookshelfListAndDetailContent(
    books:LazyPagingItems<Book>,
    bookshelfUiState: BookshelfUiState,
    textFieldParams:TextFieldParams,
    listContentParams:ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier:Modifier= Modifier
){
    Row(modifier=modifier){
        BookshelfListOnlyContent(
            books = books,
            bookshelfUiState = bookshelfUiState,
            textFieldParams=textFieldParams,
            listContentParams=listContentParams,
            modifier=Modifier.weight(1f)
        )

        val activity = LocalContext.current as Activity

        if(books.loadState.refresh is LoadState.NotLoading){
            BookshelfDetailsScreen(
                detailsScreenParams= DetailsScreenParams(
                    onBackPressed = { activity.finish() },
                    currentOrder = detailsScreenParams.currentOrder,
                    updateOrder = detailsScreenParams.updateOrder
                ),
                bookshelfUiState=bookshelfUiState,
                modifier=Modifier.weight(1f),
                isNotFullScreen = false
            )
        }

    }
}

@Composable
fun BookmarkEmptyScreen(modifier:Modifier=Modifier){
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center
    ){
        Text(
            text=stringResource(R.string.empty_bookmark),
            textAlign=TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    input:String,
    onInputChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onInputReset: (String) -> Unit
){
    OutlinedTextField(
        value = input,
        onValueChange = onInputChange,
        label={Text(stringResource(R.string.search_label))},
        leadingIcon = {
            Icon(
                imageVector= Icons.Filled.Search,
                contentDescription=stringResource(R.string.search),
                modifier= Modifier
                    .clickable { onSearch(input) }
                    .padding(
                        dimensionResource(R.dimen.list_only_content_search_icon_padding)
                    )
            )
        },
        trailingIcon={
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.search_close),
                modifier= Modifier
                    .clickable { onInputReset("") }
                    .padding(
                        dimensionResource(R.dimen.list_only_content_search_icon_padding)
                    )
            )
        },
        keyboardOptions= KeyboardOptions.Default.copy(
            imeAction= ImeAction.Search
        ),
        keyboardActions= KeyboardActions(
            onSearch = {onSearch(input)},
        ),
        modifier= Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(
                    R.dimen.list_only_content_search_horizontal_padding
                )
            )
    )
}

@Composable
private fun PageNumberButton(
    currentGroup:Int,
    pageGroupSize:Int,
    totalPages:Int,
    updatePage: (Int) -> Unit,
    currentPage: Int
) {
    Row(
        horizontalArrangement =Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.page_button_padding))
            .fillMaxWidth()
    ) {
        val startPage = currentGroup * pageGroupSize + 1
        val endPage = minOf(startPage + pageGroupSize - 1, totalPages)
        if (currentGroup > 0) {
            Text(
                text=stringResource(R.string.previous_page),
                modifier=Modifier
                    .clickable { updatePage(startPage - pageGroupSize) }
            )
        }
        for (page in startPage..endPage) {
            Text(
                text=page.toString(),
                color = if (page == currentPage) Color.Black else Color.LightGray,
                modifier=Modifier
                    .clickable { updatePage(page) }
            )
        }
        if (endPage < totalPages) {
            Text(
                text=stringResource(R.string.next_page),
                modifier=Modifier
                    .clickable { updatePage(startPage + pageGroupSize) }
            )
        }
    }
}

@Composable
private fun BookShelfListItem(
    book: Book,
    onBookItemPressed:(BookInfo)->Unit,
    onBookMarkPressed:(Book)->Unit
){
    Row(
        modifier= Modifier
            .clickable { onBookItemPressed(book.bookInfo) }
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.list_item_padding))
            .background(
                Color(
                    ContextCompat.getColor(LocalContext.current, R.color.light_gray)
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier=Modifier
                .padding(dimensionResource(R.dimen.list_item_image_padding))
        ){
            book.bookInfo.img?.let {
                AsyncImage(
                    model=ImageRequest.Builder(context=LocalContext.current)
                            .data(it.thumbnail).build(),
                    contentDescription = null,
                    contentScale= ContentScale.FillBounds,
                    modifier= Modifier
                        .height(dimensionResource(R.dimen.list_item_image_height))
                        .width(dimensionResource(R.dimen.list_item_image_width))
                )
            }
        }

        Column(
            modifier=Modifier.padding(dimensionResource(R.dimen.list_item_text_column_padding))
        ) {
            ItemDescription(book = book)
            IconButton(
                onClick = {onBookMarkPressed(book)}
            ) {
                Icon(
                    imageVector = if(book.bookInfo.isBookmarked){Icons.Default.Bookmark}
                    else {Icons.Default.BookmarkBorder},
                    contentDescription = stringResource(R.string.bookmark),
                )
            }
        }

    }
}

@Composable
private fun ItemDescription(book:Book){
    book.bookInfo.title?.let {
        Text(
            text= it,
            style = MaterialTheme.typography.bodyLarge,
            modifier=Modifier.padding(
                bottom=dimensionResource(R.dimen.list_item_text_padding)
            )
        )
    }
    Row(modifier= Modifier
        .wrapContentHeight()
        .fillMaxWidth(0.9f)){
        book.bookInfo.authors?.joinToString(separator = ",")?.let{
            Text(
                text= it,
                maxLines=1,
                style=MaterialTheme.typography.bodySmall,
                modifier=Modifier.padding(
                    bottom=dimensionResource(R.dimen.list_item_text_padding)
                )
            )
        }
    }
    book.bookInfo.publisher?.let {
        Text(
            text= it,
            style=MaterialTheme.typography.bodySmall,
            modifier=Modifier.padding(
                bottom=dimensionResource(R.dimen.list_item_text_padding)
            )
        )
    }
    book.bookInfo.publishedDate?.let {
        Text(
            text= it,
            style=MaterialTheme.typography.bodySmall
        )
    }
}