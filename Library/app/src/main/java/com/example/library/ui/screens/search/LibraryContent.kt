package com.example.library.ui.screens.search

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.ui.navigation.NavigationMenuType
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.TextFieldParams
import com.example.library.ui.screens.detail.LibraryDetailsScreen

@Composable
fun LibraryListOnlyContent(
    libraryUiState: LibraryUiState,
    books:LazyPagingItems<Book>,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    modifier:Modifier= Modifier
){
    Column(
        modifier= modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val pageGroupSize = 3
        val pageSize = PAGE_SIZE
        val totalItemCount= getTotalItemCount(libraryUiState)
        val totalPages = (totalItemCount+pageSize-1)/pageSize
        val currentGroup = (listContentParams.currentPage-1)/pageGroupSize

        SearchTextField(
            input = textFieldParams.textFieldKeyword,
            onInputChange = textFieldParams.updateKeyword,
            onSearch = textFieldParams.onSearch,
            onInputReset = textFieldParams.updateKeyword
        )

        LibraryTotalItemText(totalItemCount)

        LibraryList(
            libraryUiState= libraryUiState,
            books= books,
            pageGroupSize = pageGroupSize,
            totalPages= totalPages,
            currentGroup= currentGroup,
            listContentParams= listContentParams
        )

    }
}

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
                        dimensionResource(R.dimen.padding_xs)
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
                        dimensionResource(R.dimen.padding_xs)
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
                    R.dimen.padding_sm
                )
            )
    )
}

@Composable
private fun LibraryTotalItemText(
    totalItemCount:Int
){
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(
                    R.dimen.padding_sm
                ),
                start = dimensionResource(
                    R.dimen.padding_lg
                )
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.totalCount))
        Text(
            text = " $totalItemCount",
            modifier = Modifier.testTag(stringResource(R.string.test_itemCount))
        )
    }
}

@Composable
private fun LibraryList(
    libraryUiState: LibraryUiState,
    books:LazyPagingItems<Book>,
    pageGroupSize:Int,
    totalPages:Int,
    currentGroup: Int,
    listContentParams: ListContentParams
){
    LazyColumn(
        state=listContentParams.scrollState,
        modifier= Modifier
            .padding(dimensionResource(R.dimen.padding_md))
            .testTag(stringResource(R.string.test_list))
    ){
        if(getTabPressed(libraryUiState) == NavigationMenuType.Bookmark){
            items(getBookmarkList(libraryUiState),key={it.id}){
                listContentParams.initCurrentItem(
                    getTabPressed(libraryUiState),
                    getBookmarkList(libraryUiState)[0].bookInfo
                )
                LibraryListItem(
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
                            getTabPressed(libraryUiState), it1.bookInfo
                        )
                    }
                    LibraryListItem(
                        book = it1,
                        onBookItemPressed= listContentParams.onBookItemPressed,
                        onBookMarkPressed =listContentParams.onBookmarkPressed
                    )
                }
            }
            item{ LibraryUiStateIndicator(books) }
            item {
                PageNumberButton(
                    pageGroupSize = pageGroupSize,
                    totalPages = totalPages,
                    currentGroup = currentGroup,
                    currentPage = listContentParams.currentPage,
                    updatePage = listContentParams.updatePage
                )
            }
        }
    }
}

@Composable
private fun LibraryListItem(
    book: Book,
    onBookMarkPressed:(Book)->Unit,
    onBookItemPressed:(BookInfo)->Unit
){
    OutlinedCard{
        Row(
            modifier= Modifier
                .clickable { onBookItemPressed(book.bookInfo) }
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_sm)) ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier=Modifier
                    .height(dimensionResource(R.dimen.list_item_image_height))
                    .width(dimensionResource(R.dimen.list_item_image_width))
                    .padding(dimensionResource(R.dimen.padding_xs))
            ){
                book.bookInfo.img?.let {
                    AsyncImage(
                        model=ImageRequest.Builder(context=LocalContext.current)
                            .data(it.thumbnail).build(),
                        error= painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = null,
                        contentScale= ContentScale.FillBounds,
                        modifier=Modifier.fillMaxSize()
                    )
                }
            }

            Column(
                modifier=Modifier.padding(dimensionResource(R.dimen.padding_xs))
            ) {
                ItemDescription(book = book, onBookMarkPressed)
            }

        }
    }
}

@Composable
private fun ItemDescription(
    book: Book,
    onBookMarkPressed:(Book)->Unit
){
    book.bookInfo.title?.let {
        Text(
            text= it,
            style = MaterialTheme.typography.bodyLarge,
            modifier=Modifier
                .padding(bottom=dimensionResource(R.dimen.padding_xs))
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
                style=MaterialTheme.typography.bodySmall,
                modifier=Modifier.padding(
                    bottom=dimensionResource(R.dimen.padding_xs)
                )
            )
        }
    }
    book.bookInfo.publisher?.let {
        Text(
            text= it,
            style=MaterialTheme.typography.bodySmall,
            modifier=Modifier.padding(
                bottom=dimensionResource(R.dimen.padding_xs)
            )
        )
    }
    book.bookInfo.publishedDate?.let {
        Text(
            text= it,
            style=MaterialTheme.typography.bodySmall
        )
    }
    Text(
        text= "abc.13",
        style=MaterialTheme.typography.bodySmall
    )
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            text= "대출가능",
            style=MaterialTheme.typography.bodySmall
        )
        IconButton(
            onClick = {onBookMarkPressed(book)}
        ) {
            Icon(
                imageVector = if(book.bookInfo.isBookmarked){Icons.Default.Favorite}
                else {Icons.Default.FavoriteBorder},
                contentDescription = stringResource(R.string.bookmark),
            )
        }
    }
}

@Composable
private fun LibraryUiStateIndicator(
    books:LazyPagingItems<Book>
){
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

@Composable
private fun PageNumberButton(
    pageGroupSize:Int,
    totalPages:Int,
    currentGroup:Int,
    currentPage: Int,
    updatePage: (Int) -> Unit
) {
    Row(
        horizontalArrangement =Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_xxl))
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
                    .testTag(stringResource(R.string.test_pageNum)+page)
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
fun LibraryListAndDetailContent(
    libraryUiState: LibraryUiState,
    books:LazyPagingItems<Book>,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier:Modifier= Modifier
){
    Row(modifier=modifier){
        LibraryListOnlyContent(
            libraryUiState = libraryUiState,
            books = books,
            textFieldParams=textFieldParams,
            listContentParams=listContentParams,
            modifier=Modifier.weight(1f)
        )

        val activity = LocalContext.current as Activity

        if(books.loadState.refresh is LoadState.NotLoading){
            LibraryDetailsScreen(
                libraryUiState=libraryUiState,
                isNotFullScreen = false,
                detailsScreenParams= DetailsScreenParams(
                    isDataReadyForUi = detailsScreenParams.isDataReadyForUi,
                    updateDataReadyForUi = detailsScreenParams.updateDataReadyForUi,
                    onBackPressed = { activity.finish() }
                ),
                modifier=Modifier.weight(1f)
            )
        }

    }
}