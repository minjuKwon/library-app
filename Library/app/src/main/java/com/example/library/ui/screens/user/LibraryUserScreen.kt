package com.example.library.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.example.library.R
import com.example.library.data.entity.User
import com.example.library.ui.common.Divider
import com.example.library.ui.common.HandleUserUiState

@Composable
fun LibraryUserScreen(
    userViewModel:UserViewModel,
    userInfo: User,
    resetLiked:()->Unit,
    onNavigationToEdit:()->Unit,
    onNavigationToSetting:()->Unit,
    onNavigationToLoanHistory:()->Unit,
    onNavigationToLoanStatus:()->Unit,
    onNavigationToReservation:()->Unit,
    onNavigationToLiked:()->Unit
){

    val context = LocalContext.current
    var openAlertDialog by remember { mutableStateOf(false) }
    userViewModel.updateEmailVerifiedState(false)

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            userViewModel.updateLogInState(false)
            onNavigationToSetting()
        },
        onFailure = { state:UserUiState.Failure ->
            val message= when(state.message){
                "ERROR_INVALID_CREDENTIAL" -> R.string.invalid_password
                else -> R.string.wait
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
            Text(
                userInfo.name,
                modifier=Modifier.testTag(stringResource(R.string.test_account_name))
            )
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
                    .clickable {
                        userViewModel.signOut()
                        resetLiked()
                    }
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
                R.string.liked_list to {onNavigationToLiked()},
                R.string.unregister to {openAlertDialog=true}
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
    UnRegisterDialog(
        id= userInfo.email,
        isShow = openAlertDialog,
        onDismissRequest = {openAlertDialog=false},
        onConfirmation = {
            userViewModel.unregister(it)
            openAlertDialog=false
        }
    )
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
private fun UnRegisterDialog(
    id: String,
    isShow:Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
){
    var inputPassword by remember{ mutableStateOf("") }

    if(isShow){
        Dialog(onDismissRequest={onDismissRequest()}){
            Card {
                Column(
                    modifier=Modifier.padding(dimensionResource(R.dimen.padding_lg)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text= stringResource(R.string.unregister_dialog_content),
                        textAlign= TextAlign.Center,
                        modifier= Modifier
                            .padding(dimensionResource(R.dimen.padding_sm))
                    )
                    Text(
                        text= stringResource(R.string.id,id),
                        modifier= Modifier
                            .padding(top=dimensionResource(R.dimen.padding_lg))
                            .testTag(stringResource(R.string.test_unregister_email))
                    )
                    OutlinedTextField(
                        value=inputPassword,
                        onValueChange = {inputPassword=it},
                        label= {Text(stringResource(R.string.input_password))},
                        keyboardOptions= KeyboardOptions.Default.copy(
                            imeAction= ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions= KeyboardActions(
                            onDone = {
                                onConfirmation(inputPassword)
                            }
                        ),
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal =dimensionResource(R.dimen.padding_md),
                                vertical = dimensionResource(R.dimen.padding_lg)
                            )
                            .testTag(stringResource(R.string.test_unregister_password))
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = { onConfirmation(inputPassword) },
                    ) {
                        Text(
                            stringResource(R.string.confirm),
                            modifier=Modifier.testTag(stringResource(R.string.test_unregister_confirm))
                        )

                    }
                }
            }
        }
    }
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
                Text(
                    text=stringResource(R.string.register),
                    modifier=Modifier.testTag(stringResource(R.string.test_register))
                )
            }
        }
    }
}