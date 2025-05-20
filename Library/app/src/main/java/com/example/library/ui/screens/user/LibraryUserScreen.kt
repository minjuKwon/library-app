package com.example.library.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.library.R

@Composable
fun LibraryUserScreen(){
    Column(
        modifier=Modifier
            .padding(
                horizontal= dimensionResource(R.dimen.padding_xl),
                vertical= dimensionResource(R.dimen.padding_xxl)
            )
    ){
        Row{
            Text("김이름")
            Text(
                stringResource(R.string.edit),
                Modifier.padding(start= dimensionResource(R.dimen.padding_md))
            )
            Text(
                stringResource(R.string.log_out),
                Modifier.padding(start= dimensionResource(R.dimen.padding_md))
            )
        }

        Card(
            Modifier
                .fillMaxWidth()
                .padding(top= dimensionResource(R.dimen.padding_xl)),
        ){
            val list=listOf(
                R.string.loan_history, R.string.loan_status,
                R.string.reservation_status, R.string.unregister
            )
            Column{
                list.forEachIndexed { index, i ->
                    UserTextButton(i)
                    if(index< list.lastIndex){
                        Spacer(
                            Modifier
                                .height(dimensionResource(R.dimen.user_screen_spacer_height))
                                .fillMaxWidth()
                                .padding(horizontal=dimensionResource(R.dimen.padding_sm))
                                .background(Color.Black)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserTextButton(id:Int){
    Text(
        stringResource(id),
        Modifier.padding(dimensionResource(R.dimen.padding_sm))
    )
}

@Composable
fun NonMemberScreen(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Icon(
            imageVector = Icons.Filled.NoAccounts,
            contentDescription = null,
            modifier=Modifier
                .size(dimensionResource(R.dimen.user_screen_no_account_icons_size))
        )
        Text(
            stringResource(R.string.no_account),
            Modifier.padding(vertical=dimensionResource(R.dimen.padding_xl))
        )
        Row{
            Button(
                onClick = {},
                modifier=Modifier
                    .padding(end =dimensionResource(R.dimen.padding_xl))
            ) {
                Text(
                    text= stringResource(R.string.log_in))
            }
            Button(onClick = {}) {
                Text(stringResource(R.string.register))
            }
        }
    }
}