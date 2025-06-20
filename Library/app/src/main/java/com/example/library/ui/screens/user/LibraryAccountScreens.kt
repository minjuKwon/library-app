package com.example.library.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.library.R
import com.example.library.ui.BackIconButton
import com.example.library.ui.Divider
import com.example.library.ui.TextRadioButton

@Composable
private fun paddingModifier()= Modifier
    .padding(
        horizontal = dimensionResource(R.dimen.padding_xl),
        vertical = dimensionResource(R.dimen.padding_md)
    )

@Composable
fun LogInScreen(
    onLoggedInChange:(Boolean)->Unit,
    onBackPressed:()->Unit,
    onNavigationToSetting:()->Unit
){
    val focusRequester= remember{FocusRequester()}
    //포커스를 UI 이후 안전하게 요청할 수 있도록 설정
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var inputId by remember{mutableStateOf("")}
    var inputPassword by remember{ mutableStateOf("") }
    val focusManager= LocalFocusManager.current

    CardLayout(
        iconText = stringResource(R.string.log_in),
        onBackPressed = onBackPressed
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier= Modifier
                .padding(dimensionResource(R.dimen.padding_xxl))
        ){
            TextField(
                value=inputId,
                onValueChange = {inputId=it},
                label= {Text(stringResource(R.string.input_id))},
                keyboardOptions= KeyboardOptions.Default.copy(
                    imeAction= ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions= KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                modifier=Modifier.focusRequester(focusRequester)
            )
            TextField(
                value=inputPassword,
                onValueChange = {inputPassword=it},
                label= {Text(stringResource(R.string.input_password))},
                keyboardOptions= KeyboardOptions.Default.copy(
                    imeAction= ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions= KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                        onLoggedInChange(true)
                        onNavigationToSetting()
                    }
                ),
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(
                        top =dimensionResource(R.dimen.padding_xxl),
                        bottom = dimensionResource(R.dimen.padding_xl)
                    )
            )
            Button(onClick = {
                onLoggedInChange(true)
                onNavigationToSetting()
            }) {
                Text(
                    text=stringResource(R.string.log_in),
                    modifier=Modifier.testTag(stringResource(R.string.test_logIn))
                )
            }

        }
    }
}

@Composable
fun RegisterScreen(
    onBackPressed:()->Unit,
    onNavigationToLogIn:()->Unit
){
    val focusRequester= remember{FocusRequester()}
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    var inputName by remember{mutableStateOf("")}
    var inputId by remember{mutableStateOf("")}
    var inputPassword by remember{mutableStateOf("")}
    var inputVerifiedPassword by remember{mutableStateOf("")}
    var inputEmail by remember{mutableStateOf("")}

    val focusManager= LocalFocusManager.current

    LazyColumn{
        item{
            CardLayout(
                iconText = stringResource(R.string.register),
                onBackPressed = onBackPressed
            ){
                TextField(
                    value=inputName,
                    onValueChange = {inputName=it},
                    label= {Text(stringResource(R.string.input_name))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions= KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                    ),
                    modifier= paddingModifier()
                        .fillMaxWidth(0.7f)
                        .focusRequester(focusRequester)
                )
                Divider()

                RegisterSexAndAgeSection(focusManager)
                Divider()

                TextField(
                    value=inputId,
                    onValueChange = {inputId=it},
                    label= {Text(stringResource(R.string.input_id))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions= KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                    ),
                    modifier= paddingModifier()
                )
                TextField(
                    value=inputPassword,
                    onValueChange = {inputPassword=it},
                    label= {Text(stringResource(R.string.input_password))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions= KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                    ),
                    modifier= Modifier.padding(start= dimensionResource(R.dimen.padding_xl))
                )
                TextField(
                    value=inputVerifiedPassword,
                    onValueChange = {inputVerifiedPassword=it},
                    label= {Text(stringResource(R.string.verify_password))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions= KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                    ),
                    modifier= paddingModifier()
                )
                Divider()

                TextField(
                    value=inputEmail,
                    onValueChange = {inputEmail=it},
                    label= {Text(stringResource(R.string.input_email))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions= KeyboardActions(
                        onDone = {
                            onNavigationToLogIn()
                        },
                    ),
                    modifier= paddingModifier()
                )
                Divider()

                RegisterButton(onNavigationToLogIn)
            }
        }
    }
}

@Composable
private fun RegisterSexAndAgeSection(
    focusManager: FocusManager
){
    var inputAge by remember{mutableStateOf("")}
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier= paddingModifier()
    ){
        TextRadioButton(listOf("남","여"))
        TextField(
            value=inputAge,
            onValueChange = {inputAge=it},
            label= {Text(stringResource(R.string.input_age))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions= KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
            ),
            modifier= Modifier
                .fillMaxWidth(0.6f)
                .padding(start= dimensionResource(R.dimen.padding_xl))
        )
    }
}

@Composable
private fun RegisterButton(
    onNavigationToLogIn:()->Unit
){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier=Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_sm))
    ){
        Button(onClick = onNavigationToLogIn) {
            Text(stringResource(R.string.register))
        }
    }
}

@Composable
fun UserInformationEditScreen(
    onBackPressed:()->Unit
){
    CardLayout(
        iconText = "아이디",
        onBackPressed= onBackPressed
    ){
        EditUserNameSection()
        Divider()

        EditSexAndAgeText()
        Divider()

        Text(stringResource(R.string.change_password),modifier=paddingModifier())
        CurrentPasswordTextField()
        TextField(
            value="",
            onValueChange = {},
            label= {Text(stringResource(R.string.new_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onNext = {},
            ),
            modifier= paddingModifier()
        )
        ConfirmNewPasswordTextField()
        Divider()

        EditEmailSection()
    }
}

@Composable
private fun EditUserNameSection(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier= paddingModifier().fillMaxWidth()
    ){
        Text("김이름")
        Spacer(modifier= Modifier.weight(1f))
        Button(onClick = {}) {
            Text(stringResource(R.string.edit))
        }
    }
}

@Composable
private fun EditSexAndAgeText(){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier=paddingModifier()
    ){
        Text("남")
        Spacer(modifier= Modifier.weight(1f))
        Text("25")
        Spacer(modifier= Modifier.weight(1f))
    }
}

@Composable
private fun CurrentPasswordTextField(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal= dimensionResource(R.dimen.padding_xl))
    ){
        TextField(
            value="",
            onValueChange = {},
            label= {Text(stringResource(R.string.current_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onDone = {},
            ),
            modifier=Modifier.fillMaxWidth(0.6f)
        )
        Button(
            onClick = {},
            modifier=Modifier
                .padding(start= dimensionResource(R.dimen.padding_xl))
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Composable
private fun ConfirmNewPasswordTextField(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .fillMaxWidth()
            .padding(
                bottom= dimensionResource(R.dimen.padding_md),
                start= dimensionResource(R.dimen.padding_xl),
                end= dimensionResource(R.dimen.padding_xl)
            )
    ){
        TextField(
            value="",
            onValueChange = {},
            label= {Text(stringResource(R.string.confirm_new_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onDone = {},
            ),
            modifier=Modifier.fillMaxWidth(0.6f)
        )
        Button(
            onClick = {},
            modifier=Modifier
                .padding(start= dimensionResource(R.dimen.padding_xl))
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Composable
private fun EditEmailSection(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .fillMaxWidth()
            .padding(
                horizontal= dimensionResource(R.dimen.padding_xl),
                vertical = dimensionResource(R.dimen.padding_sm),
            )
    ){
        Text("book@mail.com")
        Spacer(modifier= Modifier.weight(1f))
        Button(onClick = {}) {
            Text(stringResource(R.string.edit))
        }
    }
}

@Composable
private fun CardLayout(
    iconText:String,
    onBackPressed:()->Unit,
    content: @Composable () -> Unit
){
    Column{
        BackIconButton(iconText){onBackPressed()}
        OutlinedCard(
            modifier= Modifier
                .padding(dimensionResource(R.dimen.padding_xl))
        ){
            content()
        }
    }
}