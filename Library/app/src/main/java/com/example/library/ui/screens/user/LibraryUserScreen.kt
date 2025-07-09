package com.example.library.ui.screens.user

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.library.R
import com.example.library.ui.Divider
import com.example.library.ui.HandleUserUiState

@Composable
fun LibraryUserScreen(
    userViewModel:UserViewModel,
    onNavigationToEdit:()->Unit,
    onNavigationToSetting:()->Unit,
    onNavigationToLoanHistory:()->Unit,
    onNavigationToLoanStatus:()->Unit,
    onNavigationToReservation:()->Unit,
    onUnregister:()->Unit,
){

    val context = LocalContext.current

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            userViewModel.updateLogInState(false)
            onNavigationToSetting()
        },
        onFailure = { state:UserUiState.Failure ->
            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
        }
    )

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
                    .testTag(stringResource(R.string.test_account_edit))
            )
            Text(
                stringResource(R.string.log_out),
                Modifier
                    .padding(start= dimensionResource(R.dimen.padding_md))
                    .clickable { userViewModel.signOut() }
                    .testTag(stringResource(R.string.test_logOut))
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
                    if(index== list.lastIndex){
                        UserTextButton(
                            i,
                            onClick,
                            Modifier.testTag(stringResource(R.string.test_unregister))
                        )
                    }else{
                        UserTextButton(i,onClick)
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
    onClick:()->Unit,
    modifier:Modifier=Modifier
){
    Text(
        stringResource(id),
        modifier
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
                .testTag(stringResource(R.string.test_noMember))
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
                    text= stringResource(R.string.log_in),
                    modifier=Modifier.testTag(stringResource(R.string.test_logIn))
                )
            }
            Button(onClick = onNavigationToRegister) {
                Text(stringResource(R.string.register))
            }
        }
    }
}