package com.example.library.ui.screens.user

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.library.R
import com.example.library.data.Gender
import com.example.library.data.User
import com.example.library.ui.BackIconButton
import com.example.library.ui.Divider
import com.example.library.ui.TextRadioButton
import kotlinx.coroutines.flow.SharedFlow

@Composable
private fun paddingModifier()= Modifier
    .padding(
        horizontal = dimensionResource(R.dimen.padding_xl),
        vertical = dimensionResource(R.dimen.padding_md)
    )

@Composable
fun LogInScreen(
    userViewModel:UserViewModel,
    onBackPressed:()->Unit,
    onNavigationToSetting:()->Unit
){
    val focusRequester= remember{FocusRequester()}
    val context= LocalContext.current

    //포커스를 UI 이후 안전하게 요청할 수 있도록 설정
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            userViewModel.updateLogInState(true)
            onNavigationToSetting()
        },
        onFailure = { state:UserUiState.Failure ->
            val message= when(state.message){
                "ERROR_INVALID_EMAIL" -> R.string.invalid_email
                "ERROR_INVALID_CREDENTIAL" -> R.string.invalid_credential
                "ERROR_TOO_MANY_REQUESTS" -> R.string.wait
                else -> R.string.fail_signIn
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    )

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
                    keyboardType = KeyboardType.Email
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
                        val result=checkLogInInputAndShowToast(context, inputId, inputPassword)
                        if(result) userViewModel.signIn(inputId, inputPassword)
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
                val result=checkLogInInputAndShowToast(context, inputId, inputPassword)
                if(result) userViewModel.signIn(inputId, inputPassword)
            }) {
                Text(
                    text=stringResource(R.string.log_in),
                    modifier=Modifier.testTag(stringResource(R.string.test_logIn))
                )
            }

        }
    }
}

private fun checkLogInInputAndShowToast(
    context:Context,
    email:String,
    password: String
):Boolean{
    when{
        email.isBlank() ->{
            Toast.makeText(context, R.string.blank_email, Toast.LENGTH_LONG).show()
            return false
        }
        password.isBlank() ->{
            Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
            return false
        }
        else->{
            Toast.makeText(context, R.string.loading_signIn, Toast.LENGTH_LONG).show()
            return true
        }
    }
}

@Composable
fun RegisterScreen(
    userViewModel:UserViewModel,
    onBackPressed:()->Unit,
    onNavigationToLogIn:()->Unit
){
    val focusRequester= remember{FocusRequester()}
    val context= LocalContext.current

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            Toast.makeText(context, R.string.success_register, Toast.LENGTH_LONG).show()
            onNavigationToLogIn()
        },
        onFailure = { state:UserUiState.Failure->
            val message= when(state.message){
                "ERROR_INVALID_EMAIL" -> R.string.invalid_email
                "ERROR_EMAIL_ALREADY_IN_USE" -> R.string.already_email
                "ERROR_WEAK_PASSWORD" -> R.string.weak_password
                else -> R.string.fail_register
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            if(state.message=="사용자 정보 저장 실패") onBackPressed()
        }
    )

    var inputEmail by remember{mutableStateOf("")}
    var inputName by remember{mutableStateOf("")}
    var inputPassword by remember{mutableStateOf("")}
    var inputVerifiedPassword by remember{mutableStateOf("")}
    var userInfo by remember{ mutableStateOf(User())}

    val focusManager= LocalFocusManager.current
    val radioText= listOf("남","여")

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

                RegisterSexAndAgeSection(
                    focusManager= focusManager,
                    list=radioText,
                    onAgeChange = {
                        userInfo = if(it.isBlank()){
                            userInfo.copy(age=0)
                        }else{
                            userInfo.copy(age=Integer.parseInt(it))
                        }
                    },
                    onSexChange = {
                        if(it == radioText[0]){
                            userInfo= userInfo.copy(gender=Gender.MALE)
                        }else if(it == radioText[1]){
                            userInfo= userInfo.copy(gender=Gender.FEMALE)
                        }
                    }
                )
                Divider()

                TextField(
                    value=inputEmail,
                    onValueChange = {inputEmail=it},
                    label= {Text(stringResource(R.string.input_email))},
                    keyboardOptions= KeyboardOptions.Default.copy(
                        imeAction= ImeAction.Next,
                        keyboardType = KeyboardType.Email
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
                        imeAction= ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions= KeyboardActions(
                        onDone = {
                            userInfo=userInfo.copy(email=inputEmail, name=inputName)
                            val result=checkRegisterInputAndShowToast(
                                context,
                                userInfo,
                                inputPassword,
                                inputVerifiedPassword
                            )
                            if(result){
                                userViewModel.register(
                                    inputPassword,
                                    userInfo
                                )
                            }
                        },
                    ),
                    modifier= paddingModifier()
                )
                Divider()

                RegisterButton(
                    onNavigationToLogIn={
                        userInfo=userInfo.copy(email=inputEmail, name=inputName)
                        val result=checkRegisterInputAndShowToast(
                            context,
                            userInfo,
                            inputPassword,
                            inputVerifiedPassword
                        )
                        if(result){
                            userViewModel.register(
                                inputPassword,
                                userInfo
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun HandleUserUiState(
    event: SharedFlow<UserUiState>,
    onSuccess:()->Unit,
    onFailure:(UserUiState.Failure)->Unit
){
    LaunchedEffect(Unit){
        event.collect{
            when(it){
                is UserUiState.Success->{
                    onSuccess()
                }
                is UserUiState.Failure -> {
                    onFailure(it)
                }
            }
        }
    }
}

@Composable
private fun RegisterSexAndAgeSection(
    focusManager: FocusManager,
    list:List<String>,
    onSexChange:(String)->Unit,
    onAgeChange:(String)->Unit
){
    var inputAge by remember{mutableStateOf("")}
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier= paddingModifier()
    ){
        TextRadioButton(list, onSexChange)
        TextField(
            value=inputAge,
            onValueChange = {
                inputAge=it
                onAgeChange(it)
            },
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

private fun checkRegisterInputAndShowToast(
    context:Context,
    user: User,
    password:String,
    verifiedPassword:String
):Boolean{
    when{
        user.name.isBlank() ->{
            Toast.makeText(context, R.string.blank_name, Toast.LENGTH_LONG).show()
            return false
        }
        user.age ==0 ->{
            Toast.makeText(context, R.string.blank_age, Toast.LENGTH_LONG).show()
            return false
        }
        user.email.isBlank() ->{
            Toast.makeText(context, R.string.blank_email, Toast.LENGTH_LONG).show()
            return false
        }
        password.isBlank() ->{
            Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
            return false
        }
        password != verifiedPassword ->{
            Toast.makeText(context, R.string.invalid_password, Toast.LENGTH_LONG).show()
            return false
        }
        else->{
            Toast.makeText(context, R.string.loading_register, Toast.LENGTH_LONG).show()
            return true
        }
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