package com.example.library.ui.screens.search

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import com.example.library.R
import com.example.library.core.PagingPolicy.PAGE_SIZE
import com.example.library.domain.DueCheckResult
import com.example.library.ui.common.LibraryListItem
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.LibraryUiModel
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.TextFieldParams
import com.example.library.ui.screens.detail.LibraryDetailsScreen
import com.example.library.ui.screens.getTotalItemCount

@Composable
fun LibraryListOnlyContent(
    libraryUiState: LibraryUiState,
    list:List<LibraryUiModel>,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    isAtRoot:Boolean,
    isNotFullScreen:Boolean,
    isLogIn:Boolean,
    onNavigateToDetails:(String)->Unit,
    onNavigationToLogIn:()->Unit,
    modifier:Modifier= Modifier
){
    var openAlertFirst by rememberSaveable { mutableStateOf(true) }
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier= modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pageGroupSize = 3
        val pageSize = PAGE_SIZE
        val totalItemCount= getTotalItemCount(libraryUiState)
        val totalPages = (totalItemCount+pageSize-1)/pageSize
        val currentGroup = (listContentParams.currentPage-1)/pageGroupSize

        HandleDoubleBackToExit(isAtRoot,listContentParams)

        SearchTextField(
            input = textFieldParams.textFieldKeyword,
            onInputChange = textFieldParams.updateKeyword,
            onSearch = textFieldParams.onSearch,
            onInputReset = textFieldParams.updateKeyword
        )

        LibraryTotalItemText(totalItemCount)

        LibraryList(
            libraryUiState= libraryUiState,
            list= list,
            pageGroupSize = pageGroupSize,
            totalPages= totalPages,
            currentPage= listContentParams.currentPage,
            currentGroup= currentGroup,
            listContentParams= listContentParams,
            isNotFullScreen=isNotFullScreen,
            isLogIn= isLogIn,
            onNavigateToDetails= onNavigateToDetails,
            onNavigationToLogIn= onNavigationToLogIn
        )

    }

    val dueCheckResult= listContentParams.dueCheckResult
    if(dueCheckResult.overdue.isNotEmpty()||dueCheckResult.today.isNotEmpty()){
        openAlertDialog=true
    }

    UserLoanStatusDialog(
        isShow = openAlertDialog&&openAlertFirst,
        dueCheckResult = dueCheckResult,
        onDismissRequest = {
            openAlertDialog=false
            openAlertFirst=false
        },
    )
}

@Composable
private fun HandleDoubleBackToExit(
    isAtRoot: Boolean,
    listContentParams: ListContentParams
){
    val context= LocalContext.current

    if(isAtRoot){
        BackHandler {
            val currentTime= System.currentTimeMillis()
            if(listContentParams.isBackPressedDouble()){
                (context as Activity).finish()
            }else{
                listContentParams.updateBackPressedTime(currentTime)
                Toast.makeText(context,R.string.toast_finish,Toast.LENGTH_LONG).show()
            }
        }
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
                top = dimensionResource(R.dimen.padding_lg),
                start = dimensionResource(R.dimen.padding_lg),
                end = dimensionResource(R.dimen.padding_lg)
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
                top = dimensionResource(R.dimen.padding_md),
                start = dimensionResource(R.dimen.padding_xl),
                end = dimensionResource(R.dimen.padding_xl)
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
    list:List<LibraryUiModel>,
    pageGroupSize:Int,
    totalPages:Int,
    currentPage:Int,
    currentGroup: Int,
    listContentParams: ListContentParams,
    isNotFullScreen:Boolean,
    isLogIn:Boolean,
    onNavigateToDetails:(String)->Unit,
    onNavigationToLogIn:()->Unit,
){
    val prePage = remember { mutableIntStateOf(0) }

    LazyColumn(
        state=listContentParams.scrollState,
        modifier= Modifier
            .padding(dimensionResource(R.dimen.padding_lg))
            .testTag(stringResource(R.string.test_list))
    ){
        items(count=list.size){
            list[it].let { it1 ->
                if(it==0&&prePage.intValue != currentPage){
                    listContentParams.updateCurrentBook(it1.library)
                    prePage.intValue = currentPage
                }
                LibraryListItem(
                    libraryUiModel = it1,
                    onBookItemPressed= listContentParams.onBookItemPressed,
                    onLikedPressed =listContentParams.onLikedPressed,
                    isNotFullScreen=isNotFullScreen,
                    isLogIn= isLogIn,
                    onNavigateToDetails= onNavigateToDetails,
                    onNavigationToLogIn= onNavigationToLogIn
                )
            }
        }
        item{ LibraryUiStateIndicator(libraryUiState) }
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

@Composable
private fun LibraryUiStateIndicator(
    libraryUiState: LibraryUiState,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier=Modifier.fillMaxWidth()
    ){
        when{
            libraryUiState is LibraryUiState.Loading -> {
                CircularProgressIndicator()
            }
            libraryUiState is LibraryUiState.Error ->{
                Text(stringResource(R.string.load_data_error))
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
                modifier= Modifier
                    .clickable { updatePage(page) }
                    .testTag(stringResource(R.string.test_pageNum) + page)
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
private fun UserLoanStatusDialog(
    isShow:Boolean,
    dueCheckResult: DueCheckResult,
    onDismissRequest: () -> Unit
){
    var titleText=""
    val contentText= StringBuilder()

    if(dueCheckResult.overdue.isNotEmpty()){
        titleText= stringResource(
            R.string.loan_status_dialog_content_overdue,
            dueCheckResult.overdue.size
        )
        dueCheckResult.overdue.forEach { contentText.append(it.title).append("\n")}
    }else if(dueCheckResult.today.isNotEmpty()){
        titleText= stringResource(
            R.string.loan_status_dialog_content_today,
            dueCheckResult.today.size
        )
        dueCheckResult.today.forEach { contentText.append(it.title).append("\n")}
    }

    if(isShow){
        Dialog(onDismissRequest={onDismissRequest()}){
            Card{
                Column {
                    Text(text=titleText)
                    Text(text=contentText.toString())
                    TextButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryListAndDetailContent(
    libraryUiState: LibraryUiState,
    isAtRoot:Boolean,
    isLogIn: Boolean,
    list:List<LibraryUiModel>,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    onNavigateToDetails:(String)->Unit,
    onNavigationToLogIn:()->Unit,
    modifier:Modifier= Modifier
){
    Row(modifier=modifier){
        LibraryListOnlyContent(
            libraryUiState = libraryUiState,
            list = list,
            textFieldParams=textFieldParams,
            listContentParams=listContentParams,
            onNavigateToDetails = onNavigateToDetails,
            onNavigationToLogIn=onNavigationToLogIn,
            isAtRoot=isAtRoot,
            isNotFullScreen=false,
            isLogIn = isLogIn,
            modifier=Modifier.weight(1f)
        )

        val activity = LocalContext.current as Activity

        if(libraryUiState is LibraryUiState.Success){
            LibraryDetailsScreen(
                isLogIn=isLogIn,
                isNotFullScreen = false,
                detailsScreenParams= detailsScreenParams,
                onBackPressed= { activity.finish() },
                modifier=Modifier.weight(1f)
            )
        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier=Modifier){
    Image(
        painter= painterResource(R.drawable.baseline_rotate_left_24),
        contentDescription=stringResource(R.string.loading),
        modifier=modifier.size(dimensionResource(R.dimen.loading_screen_image_size))
    )
}

@Composable
fun ErrorScreen(
    input:String,
    retryAction:(String)->Unit,
    modifier: Modifier=Modifier
){
    Column(
        modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.baseline_error_outline_24),
            contentDescription = stringResource(R.string.error)
        )
        Text(
            text=stringResource(R.string.error),
            modifier=Modifier.padding(dimensionResource(R.dimen.padding_md))
        )
        Button(onClick = {retryAction(input)}) {
            Text(text=stringResource(R.string.retry))
        }
    }
}