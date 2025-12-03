package com.example.library.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.library.R
import com.example.library.ui.common.BackIconButton
import com.example.library.ui.common.Divider
import com.example.library.ui.common.LibraryListItem
import com.example.library.ui.common.LibraryUiModel
import com.example.library.ui.common.ListContentParams

@Composable
fun LoanHistoryScreen(
    onBackPressed:()->Unit
){
    val textList= listOf(
        R.string.index, R.string.title, R.string.author,
        R.string.loan_date, R.string.return_date, R.string.status
    )
    val temp= listOf(
        "1","제목제목제목제목제목제목제목제","김저자1,이저자2,박저자3",
        "2025-\n00-00","2025-\n00-00","반납\n완료"
    )

    UserUsageSection(
        screenTitle = stringResource(R.string.loan_history),
        titleList = textList,
        contentList = temp,
        onBackPressed=onBackPressed
    )
}

@Composable
fun LoanStatusScreen(
    onBackPressed:()->Unit
){
    val textList= listOf(
        R.string.index, R.string.title, R.string.author,
        R.string.loan_date, R.string.expected_return_date, R.string.status
    )
    val temp= listOf(
        "1","제목제목제목제목제목제목제목제","김저자1,이저자2,박저자3",
        "2025-\n00-00","2025-\n00-00","반납\n완료"
    )

    UserUsageSection(
        screenTitle = stringResource(R.string.loan_status),
        titleList = textList,
        contentList = temp,
        onBackPressed=onBackPressed
    ){
        LoanStatusSection()
    }
}

@Composable
private fun LoanStatusSection(){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier=Modifier
            .fillMaxWidth()
            .padding(
                top=dimensionResource(R.dimen.padding_xl),
                start = dimensionResource(R.dimen.padding_xs),
                end = dimensionResource(R.dimen.padding_xs)
        )
    ){
        LoanStatusSectionCard(
            R.string.borrowed_book,
            "2",
            Modifier.padding(end= dimensionResource(R.dimen.padding_md))
        )
        LoanStatusSectionCard(
            R.string.number_of_overdue_book,
            "0",
            Modifier.padding(end= dimensionResource(R.dimen.padding_md))
        )
        LoanStatusSectionCard(
            R.string.suspension_end,
            "0"
        )
    }
}

@Composable
private fun LoanStatusSectionCard(
    textTitleId:Int,
    textContent:String,
    modifier:Modifier=Modifier
){
    Card(modifier=modifier){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier=Modifier
                .height(dimensionResource(R.dimen.loan_status_section_card_height))
                .padding(horizontal = dimensionResource(R.dimen.padding_md))
        ) {
            Text(
                stringResource(textTitleId),
                modifier=Modifier
                    .padding(bottom= dimensionResource(R.dimen.padding_sm)),
                textAlign = TextAlign.Center
            )
            Text(textContent)
        }
    }
}

@Composable
fun ReservationStatusScreen(
    onBackPressed:()->Unit
){
    val textList= listOf(
        R.string.index, R.string.title, R.string.author,
        R.string.reservation_date, R.string.reservation_rank, R.string.is_reserved
    )
    val temp= listOf(
        "1","제목제목제목제목제목제목제목제","김저자1,이저자2,박저자3",
        "2025-00-00","1","예약중"
    )

    UserUsageSection(
        screenTitle=stringResource(R.string.reservation_status),
        titleList=textList,
        contentList=temp,
        onBackPressed=onBackPressed
    )
}

@Composable
fun LikedListScreen(
    list:List<LibraryUiModel>,
    listContentParams: ListContentParams,
    onBackPressed:()->Unit,
    onNavigateToDetails:(String)->Unit,
){
    Column{
        BackIconButton(stringResource(R.string.liked_list)){onBackPressed()}

        LazyColumn(
            modifier= Modifier
                .padding(
                    top= dimensionResource(R.dimen.padding_lg),
                    start = dimensionResource(R.dimen.padding_xxl),
                    end = dimensionResource(R.dimen.padding_xxl)
                )
                .testTag(stringResource(R.string.test_list))
        ){
            items(list.size){
                LibraryListItem(
                    libraryUiModel=list[it],
                    onLikedPressed=listContentParams.onLikedPressed,
                    onBookItemPressed=listContentParams.onBookItemPressed,
                    onNavigateToDetails=onNavigateToDetails,
                    isShowLibraryInfo=true
                )
            }
        }
    }
}

@Composable
private fun UserUsageSection(
    screenTitle:String,
    titleList:List<Int>,
    contentList:List<String>,
    weightList:List<Float> =listOf(0.1f, 0.2f, 0.2f, 0.18f, 0.18f, 0.14f),
    onBackPressed:()->Unit,
    extraContent:@Composable () -> Unit ={}
){
    Column{
        BackIconButton(screenTitle){onBackPressed()}
        extraContent()
        Column(
            modifier=Modifier.padding(
                top=dimensionResource(R.dimen.padding_lg),
                bottom=dimensionResource(R.dimen.padding_xl),
                start = dimensionResource(R.dimen.padding_xxl),
                end = dimensionResource(R.dimen.padding_xxl)
            )
        ){
            UserUsageList(titleList, contentList, weightList)
        }
    }
}

@Composable
private fun UserUsageList(
    titleList:List<Int>,
    contentList:List<String>,
    weightList:List<Float>
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_xs))
    ){
        titleList.zip(weightList).forEach { (text,weight) ->
            Text(
                text= stringResource(text),
                modifier=Modifier.weight(weight),
                textAlign= TextAlign.Center
            )
        }
    }
    Divider()
    LazyColumn {
        items(5){
            UserUsageListItem(contentList, weightList)
        }
    }
}

@Composable
private fun UserUsageListItem(
    contentList:List<String>,
    weightList: List<Float>
){
    OutlinedCard(
        modifier=Modifier.padding(
            top= dimensionResource(R.dimen.padding_md)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier=Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_sm),
                vertical = dimensionResource(R.dimen.padding_xs)
            )
        ){
            contentList.zip(weightList).forEach{ (text, weight) ->
                Text(
                    text= text,
                    modifier=Modifier.weight(weight),
                    textAlign= TextAlign.Center
                )
            }
        }
    }
}