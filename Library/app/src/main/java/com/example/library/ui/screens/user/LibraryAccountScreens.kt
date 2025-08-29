package com.example.library.ui.screens.user

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.library.R
import com.example.library.data.Gender
import com.example.library.data.User
import com.example.library.ui.BackIconButton
import com.example.library.ui.Divider
import com.example.library.ui.HandleUserUiState
import com.example.library.ui.TextRadioButton

@Composable
private fun paddingModifier()= Modifier
    .padding(
        horizontal = dimensionResource(R.dimen.padding_xl),
        vertical = dimensionResource(R.dimen.padding_md)
    )

@Composable
fun LogInScreen(
    userViewModel:UserViewModel,
    isClickEmailLink:Boolean,
    onBackPressed:()->Unit,
    onNavigationToSetting:()->Unit
){
    val context= LocalContext.current
    val focusManager= LocalFocusManager.current

    val focusRequester= remember{FocusRequester()}

    var isClick by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var inputId by remember{mutableStateOf("")}
    var inputPassword by remember{ mutableStateOf("") }

    //포커스를 UI 이후 안전하게 요청할 수 있도록 설정
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        isClick=false
    }

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            isClick=true
            userViewModel.updateLogInState(true)
            onNavigationToSetting()
        },
        onFailure = { state:UserUiState.Failure ->
            if(state.message=="사용자 인증 실패") userViewModel.checkUserVerified()
            else{
                val message= when(state.message){
                    "ERROR_INVALID_EMAIL" -> R.string.invalid_email
                    "ERROR_INVALID_CREDENTIAL" -> R.string.invalid_credential
                    "ERROR_TOO_MANY_REQUESTS" -> R.string.wait
                    else -> R.string.fail_signIn
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    )

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
            if(userViewModel.isUserVerified.value&&isClickEmailLink){
                Text(
                    text= stringResource(R.string.already_reauthorization),
                    modifier=Modifier.padding(dimensionResource(R.dimen.padding_sm))
                )
            }
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
                        if(!isClick){
                            val result=checkLogInInputAndShowToast(context, inputId, inputPassword)
                            if(result){
                                userViewModel.signIn(inputId, inputPassword)
                            }
                        }
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
                if(!isClick){
                    val result=checkLogInInputAndShowToast(context, inputId, inputPassword)
                    if(result){
                        userViewModel.signIn(inputId, inputPassword)
                    }
                }
            }) {
                Text(
                    text=stringResource(R.string.log_in),
                    modifier=Modifier.testTag(stringResource(R.string.test_logIn))
                )
            }
            Text(
                text= stringResource(R.string.find_password),
                modifier= Modifier
                    .padding(dimensionResource(R.dimen.padding_md))
                    .clickable { openAlertDialog=true }
            )
        }
    }
    FindPasswordDialog(
        isShow = openAlertDialog,
        onDismissRequest = {openAlertDialog=false},
        onConfirmation = {
            userViewModel.findPassword(it)
            openAlertDialog=false
        }
    )
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
private fun FindPasswordDialog(
    isShow:Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
){
    var inputEmail by remember{ mutableStateOf("") }

    if(isShow){
        Dialog(onDismissRequest={onDismissRequest()}){
            Card {
                Column(
                    modifier=Modifier.padding(dimensionResource(R.dimen.padding_lg)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text= stringResource(R.string.find_password_dialog_content),
                        textAlign= TextAlign.Center,
                        modifier= Modifier
                            .padding(dimensionResource(R.dimen.padding_lg))
                    )
                    OutlinedTextField(
                        value=inputEmail,
                        onValueChange = {inputEmail=it},
                        label= {Text(stringResource(R.string.input_email))},
                        keyboardOptions= KeyboardOptions.Default.copy(
                            imeAction= ImeAction.Done,
                            keyboardType = KeyboardType.Email
                        ),
                        keyboardActions= KeyboardActions(
                            onDone = {
                                onConfirmation(inputEmail)
                            }
                        ),
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal =dimensionResource(R.dimen.padding_md),
                                vertical = dimensionResource(R.dimen.padding_lg)
                            )
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
                        onClick = { onConfirmation(inputEmail) },
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
fun NotVerificationScreen(
    userViewModel:UserViewModel
){
    val context = LocalContext.current
    val lifecycleOwner = (LocalContext.current as ComponentActivity).lifecycle
    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            when(event){
                Lifecycle.Event.ON_RESUME ->{ userViewModel.checkUserVerified() }
                else ->{}
            }
        }
        lifecycleOwner.addObserver(observer)
        onDispose {
            lifecycleOwner.removeObserver(observer)
        }
    }

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            userViewModel.updateEmailVerifiedState(true)
            Toast.makeText(context, R.string.send_email, Toast.LENGTH_LONG).show()
        },
        onFailure = {
            Toast.makeText(context, R.string.wait, Toast.LENGTH_LONG).show()
        }
    )

    Column(
        modifier=Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector =  Icons.Default.DoNotDisturbOn,
            contentDescription = null,
            modifier=Modifier.size(dimensionResource(R.dimen.not_verification_image_size))
        )
        Text(
            text= stringResource(R.string.retry_verification),
            modifier=Modifier.padding(dimensionResource(R.dimen.padding_md))
        )
        Button(
            onClick = { userViewModel.sendVerificationEmail() }
        ) {
            Text(
                text= stringResource(R.string.resend)
            )
        }
    }
}

@Composable
fun RegisterScreen(
    userViewModel:UserViewModel,
    onBackPressed:()->Unit,
    onNavigationToLogIn:()->Unit
){
    val context= LocalContext.current
    val focusManager= LocalFocusManager.current

    val radioText= listOf("남","여")
    val focusRequester= remember{FocusRequester()}

    var userInfo by remember{ mutableStateOf(User())}
    var isClick by remember { mutableStateOf(false) }
    var inputEmail by remember{mutableStateOf("")}
    var inputName by remember{mutableStateOf("")}
    var inputPassword by remember{mutableStateOf("")}
    var inputVerifiedPassword by remember{mutableStateOf("")}

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
        isClick=false
    }

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            isClick=true
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
                            if(!isClick){
                                userInfo=userInfo.copy(email=inputEmail, name=inputName)
                                val result=checkRegisterInputAndShowToast(
                                    context,
                                    userInfo,
                                    inputPassword,
                                    inputVerifiedPassword
                                )
                                if(result){
                                    userViewModel.register(
                                        userInfo,
                                        inputPassword
                                    )
                                }
                            }
                        },
                    ),
                    modifier= paddingModifier()
                )
                Divider()

                RegisterButton(
                    onNavigationToLogIn={
                        if(!isClick){
                            userInfo=userInfo.copy(email=inputEmail, name=inputName)
                            val result=checkRegisterInputAndShowToast(
                                context,
                                userInfo,
                                inputPassword,
                                inputVerifiedPassword
                            )
                            if(result){
                                userViewModel.register(
                                    userInfo,
                                    inputPassword
                                )
                            }
                        }
                    }
                )
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
    userViewModel:UserViewModel,
    onBackPressed:()->Unit,
    onNavigationToSetting:()->Unit
){
    val context= LocalContext.current
    val focusManager= LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester= remember{FocusRequester()}
    val isPasswordVerified by userViewModel.isPasswordVerified

    var isClickName by remember { mutableStateOf(false) }
    var isClickCurrentPassword by remember { mutableStateOf(false) }
    var isClickNewPassword by remember { mutableStateOf(false) }
    var inputNewPassword by remember{ mutableStateOf("") }

    LaunchedEffect(Unit) {
        isClickName=false
        isClickCurrentPassword=false
        isClickNewPassword=false
        userViewModel.updatePasswordVerifiedState(false)
    }

    HandleUserUiState(
        event= userViewModel.event,
        onSuccess = {
            isClickName=true
            isClickNewPassword=true
            Toast.makeText(context, R.string.success_edit, Toast.LENGTH_LONG).show()
            onNavigationToSetting()
        },
        onFailure = { state:UserUiState.Failure ->
            val message= when(state.message){
                "ERROR_INVALID_CREDENTIAL" -> R.string.invalid_password
                "ERROR_WEAK_PASSWORD" -> R.string.weak_password
                else -> R.string.wait
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    )

    CardLayout(
        iconText = "아이디",
        onBackPressed= onBackPressed
    ){
        EditUserNameSection(
            context= context,
            onEdit = {
                if(!isClickName){
                    userViewModel.changeUserInfo(mapOf("name" to it))
                }
            }
        )
        Divider()

        EditSexAndAgeText()
        Divider()

        Text(stringResource(R.string.change_password),modifier=paddingModifier())
        CurrentPasswordTextField(
            context=context,
            onVerify = {
                if(isPasswordVerified){
                    Toast.makeText(context, R.string.already_reauthorization,Toast.LENGTH_LONG).show()
                }else{
                    if(!isClickCurrentPassword){
                        keyboardController?.hide()
                        userViewModel.verifyCurrentPassword(it)
                        isClickCurrentPassword=true
                        Toast.makeText(context, R.string.already_reauthorization,Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        TextField(
            value=inputNewPassword,
            onValueChange = {inputNewPassword=it},
            label= {Text(stringResource(R.string.new_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Next)},
            ),
            modifier= paddingModifier().focusRequester(focusRequester)
        )
        ConfirmNewPasswordTextField(
            context=context,
            newPassword = inputNewPassword,
            onConfirm = {
                if(isPasswordVerified){
                    if(!isClickNewPassword){
                        keyboardController?.hide()
                        userViewModel.changePassword(it)
                    }
                }else{
                    Toast.makeText(context, R.string.check_password, Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}

@Composable
private fun EditUserNameSection(
    context: Context,
    onEdit:(String)->Unit
){
    var inputName by remember{mutableStateOf("김이름")}

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier= paddingModifier().fillMaxWidth()
    ){
        TextField(
            value=inputName,
            onValueChange = {inputName=it},
            label= {Text(stringResource(R.string.input_name))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions= KeyboardActions(
                onNext = {
                    if(inputName.isBlank()){
                        Toast.makeText(context, R.string.blank_name, Toast.LENGTH_LONG).show()
                    }else{
                        onEdit(inputName)
                    }
                },
            ),
            modifier= Modifier
                .weight(0.6f)
                .padding(
                    end = dimensionResource(R.dimen.padding_xl),
                    top = dimensionResource(R.dimen.padding_md),
                    bottom = dimensionResource(R.dimen.padding_md)
                )
        )
        Button(onClick = {
            if(inputName.isBlank()){
                Toast.makeText(context, R.string.blank_name, Toast.LENGTH_LONG).show()
            }else{
                onEdit(inputName)
            }
        }) {
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
private fun CurrentPasswordTextField(
    context: Context,
    onVerify:(String)->Unit
){
    var inputPassword by remember{mutableStateOf("")}

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal= dimensionResource(R.dimen.padding_xl))
    ){
        TextField(
            value=inputPassword,
            onValueChange = {inputPassword=it},
            label= {Text(stringResource(R.string.current_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onDone = {
                    if(inputPassword.isBlank()){
                        Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
                    }else{
                        onVerify(inputPassword)
                    }
                },
            ),
            modifier=Modifier.fillMaxWidth(0.6f)
        )
        Button(
            onClick = {
                if(inputPassword.isBlank()){
                    Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
                }else{
                    onVerify(inputPassword)
                }
            },
            modifier=Modifier
                .padding(start= dimensionResource(R.dimen.padding_xl))
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Composable
private fun ConfirmNewPasswordTextField(
    context: Context,
    newPassword:String,
    onConfirm:(String)->Unit,
){
    var inputPassword by remember{mutableStateOf("")}

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
            value=inputPassword,
            onValueChange = {inputPassword=it},
            label= {Text(stringResource(R.string.confirm_new_password))},
            keyboardOptions= KeyboardOptions.Default.copy(
                imeAction= ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions= KeyboardActions(
                onDone = {
                    if(inputPassword.isBlank()){
                        Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
                    }else if(inputPassword!=newPassword){
                        Toast.makeText(context, R.string.incorrect_new_password, Toast.LENGTH_LONG).show()
                    }else{
                        onConfirm(inputPassword)
                    }
                },
            ),
            modifier=Modifier.fillMaxWidth(0.6f)
        )
        Button(
            onClick = {
                if(inputPassword.isBlank()){
                    Toast.makeText(context, R.string.blank_password, Toast.LENGTH_LONG).show()
                }else if(inputPassword!=newPassword){
                    Toast.makeText(context, R.string.incorrect_new_password, Toast.LENGTH_LONG).show()
                }else{
                    onConfirm(inputPassword)
                }
            },
            modifier=Modifier
                .padding(start= dimensionResource(R.dimen.padding_xl))
        ) {
            Text(stringResource(R.string.confirm))
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