package com.presentation.login.scenes.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import preset_ui.icons.CloseLine
import com.presentation.login.scenes.sign_up.SignUpDialogViewModel.Action.*

@Composable
fun SignUpDialog(
    onDismiss: () -> Unit,
    viewModel: SignUpDialogViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        content(onDismiss = { }, viewModel = viewModel)
    }
}

@Composable
private fun content(
    onDismiss: () -> Unit,
    viewModel: SignUpDialogViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.background(CS.Gray.White)
    ) {
        TopAppBar { onDismiss() }
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(all = 20.dp)
        ) {
            NicknameInput(
                value = uiState.nickname,
                onValueChange = { viewModel.handleAction(UpdateNickNameTextField, it) },
                onCheckDuplicate = { viewModel.handleAction(DidTapCheckDuplicateButton) }
            )

            

        }

        Spacer(Modifier.weight(1f))
        SubmitButton(
            isEnabled = uiState.isSubmitEnabled,
            onClick = { viewModel.handleAction(DidTapSubmitButton) }
        )
    }
}

@Composable
fun NicknameInput(
    value: String,
    onValueChange: (String) -> Unit,
    onCheckDuplicate: () -> Unit,
    isError: Boolean = false,
    helperText: String = "닉네임은 최소 2자 이상 15자 이내로 입력해주세요."
) {
    Column {
        Text(text = "닉네임", style = Typography.h3, color = CS.Gray.G90)
        Spacer(Modifier.height(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    width = 1.dp,
                    color = if (isError) CS.System.Red else CS.Gray.G20,
                    shape = RoundedCornerShape(8.dp)
                ),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        if (value.isEmpty()) {
                            Text(text = "닉네임", style = Typography.body1Regular, color = CS.Gray.G40)
                        }
                        innerTextField()
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = onCheckDuplicate,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 5.5.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CS.PrimaryOrange.O40,
                            contentColor = CS.Gray.White
                        ),
                        elevation = null
                    ) {
                        Text("중복 확인", style = Typography.body2Regular)
                    }
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        Text(text = helperText, style = Typography.captionRegular, color = if (isError) CS.System.Red else CS.Gray.G50)
    }
}


@Composable
private fun TopAppBar(onCloseButtonClick: () -> Unit) {
    DefaultTopAppBar(
        title = "회원 가입",
        actions = {
            IconButton(
                onClick = { onCloseButtonClick() }
            ) {
                CloseLine(24.dp, 24.dp, tint = CS.Gray.G90)
            }
        }
    )
}

@Composable
private fun SubmitButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 5.dp)
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White,
            disabledContainerColor = CS.PrimaryOrange.O20,
            disabledContentColor = CS.Gray.White
        ),
        elevation = null
    ) {
        Text(text = "가입하기", style = Typography.body1Bold)
    }
}