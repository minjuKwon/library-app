package com.example.library.ui.screens.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.library.R
import com.example.library.ui.Divider

@Composable
fun LibraryUserScreen(
    onNavigationToEdit:()->Unit,
    onLogOut:()->Unit,
    onNavigationToLoanHistory:()->Unit,
    onNavigationToLoanStatus:()->Unit,
    onNavigationToReservation:()->Unit,
    onUnregister:()->Unit,
){
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
                stringResource(R.string.edit_information),
                Modifier
                    .padding(start= dimensionResource(R.dimen.padding_md))
                    .clickable { onNavigationToEdit() }
            )
            Text(
                stringResource(R.string.log_out),
                Modifier
                    .padding(start= dimensionResource(R.dimen.padding_md))
                    .clickable { onLogOut() }
            )
        }

        Card(
            Modifier
                .fillMaxWidth()
                .padding(top= dimensionResource(R.dimen.padding_xl)),
        ){
            val list=listOf(
                R.string.loan_history to {onNavigationToLoanHistory()},
                R.string.loan_status to {onNavigationToLoanStatus()},
                R.string.reservation_status to {onNavigationToReservation()},
                R.string.unregister to {onUnregister()}
            )
            Column{
                list.forEachIndexed { index, (i, onClick) ->
                    UserTextButton(i,onClick)
                    if(index< list.lastIndex){
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun UserTextButton(
    id:Int,
    onClick:()->Unit
){
    Text(
        stringResource(id),
        Modifier
            .padding(dimensionResource(R.dimen.padding_sm))
            .clickable { onClick() }
    )
}

@Composable
fun NonMemberScreen(
    onNavigationToLogIn:()->Unit,
    onNavigationToRegister:()->Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=Modifier.fillMaxWidth()
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
                onClick = onNavigationToLogIn,
                modifier=Modifier
                    .padding(end =dimensionResource(R.dimen.padding_xl))
            ) {
                Text(
                    text= stringResource(R.string.log_in))
            }
            Button(onClick = onNavigationToRegister) {
                Text(stringResource(R.string.register))
            }
        }
    }
}