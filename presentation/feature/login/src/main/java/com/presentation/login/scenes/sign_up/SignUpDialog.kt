package com.presentation.login.scenes.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import preset_ui.icons.CloseLine
import com.presentation.login.scenes.sign_up.SignUpDialogViewModel.Action.*
import preset_ui.SimpleTextFieldButton
import preset_ui.SimpleTextFieldOutlinedButton
import preset_ui.icons.RightArrowIcon
import preset_ui.icons.SignUpRemove

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
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            NicknameInput(
                value = uiState.nickname,
                onValueChange = { viewModel.handleAction(UpdateNickNameTextField, it) },
                onCheckDuplicate = { viewModel.handleAction(DidTapCheckDuplicateButton) }
            )
            MyTownInput(
                value = uiState.myTown,
                onClick = { viewModel.handleAction(DidTapMyTownTextFieldButton) }
            )
            InterestInput(
                towns = uiState.interestTowns,
                onRemoveButtonClick = { viewModel.handleAction(DidTapRemoveInterestTownButton) },
                onInterestTownItemClick = { viewModel.handleAction(DidTapAddInterestTownButton) },
                onAddTownButtonClick = { viewModel.handleAction(DidTapAddInterestTownButton) }
            )
            TermsSection(
                terms = uiState.terms,
                onCheckBoxButtonClick = { viewModel.handleAction(DidTapCheckBox, it) },
                onDetailButtonClick = { viewModel.handleAction(DidTapDetailButton, it) },
            )
        }
        SubmitButton(
            isEnabled = uiState.isSubmitEnabled,
            onClick = { viewModel.handleAction(DidTapSubmitButton) }
        )
    }
}

@Composable
private fun NicknameInput(
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
        Text(
            text = helperText,
            style = Typography.captionRegular,
            color = if (isError) CS.System.Red else CS.Gray.G50
        )
    }
}

@Composable
private fun MyTownInput(
    value: String,
    onClick: () -> Unit
) {
    Column {
        Text(text = "우리 동네 설정", style = Typography.h3, color = CS.Gray.G90)
        Spacer(Modifier.height(10.dp))
        SimpleTextFieldOutlinedButton (
            value = value,
            placeholder = "동 검색하기",
            selectableMark = false,
            onClick = onClick
        )
    }
}

@Composable
private fun InterestInput(
    towns: List<String>,
    onInterestTownItemClick: (String) -> Unit,
    onRemoveButtonClick: (String) -> Unit,
    onAddTownButtonClick: () -> Unit
) {
    Column {
        Text(text = "관심 동네 설정 (선택)", style = Typography.h3, color = CS.Gray.G90)
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(towns, key = { it }) { town ->
                DeletableTownChip(
                    text = town,
                    onItemClick = { onInterestTownItemClick(town) },
                    onDelete = { onRemoveButtonClick(town) }
                )
            }

            item {
                AddTownChip(
                    onClick = { onAddTownButtonClick() }
                )
            }
        }
    }
}

@Composable
private fun DeletableTownChip(
    text: String,
    onItemClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .border(width = 1.dp, color = CS.PrimaryOrange.O40, shape = RoundedCornerShape(8.dp))
            .background(color = CS.PrimaryOrange.O10, shape = RoundedCornerShape(8.dp))
            .clickable { onItemClick() }
            .zIndex(2f)
    ) {
        Text(
            text = text,
            style = Typography.body1Bold,
            color = CS.PrimaryOrange.O40,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 20.dp)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopEnd)
                .offset(x = 5.dp, y = (-5).dp)
                .zIndex(1f)
        ) {
            SignUpRemove(18.dp, 18.dp)
        }
    }
}

@Composable
private fun AddTownChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White
        ),
        elevation = null,
        modifier = modifier.height(48.dp)
    ) {
        Text("추가하기", style = Typography.body2Bold)
    }
}


enum class TermKind { ALL, SERVICE, PRIVACY, LOCATION }
@Composable
fun TermsSection(
    terms: TermsAgreement,
    onCheckBoxButtonClick: (TermKind) -> Unit,
    onDetailButtonClick: (TermKind) -> Unit,
) {
    Column() {
        TermRow(
            checked = terms.all,
            label = "약관 전체 동의",
            kind = TermKind.ALL,
            onCheckboxButtonClick = { onCheckBoxButtonClick(TermKind.ALL) },
            onDetailButtonClick = {} // 없음
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = CS.Gray.G20)
        TermRow(
            checked = terms.service,
            label = "[필수] 서비스 이용 약관 동의",
            kind = TermKind.SERVICE,
            onCheckboxButtonClick = { onCheckBoxButtonClick(TermKind.SERVICE) },
            onDetailButtonClick = { onDetailButtonClick(TermKind.SERVICE) }
        )

        TermRow(
            checked = terms.privacy,
            label = "[필수] 개인정보 수집 및 이용 동의",
            kind = TermKind.PRIVACY,
            onCheckboxButtonClick = { onCheckBoxButtonClick(TermKind.PRIVACY) },
            onDetailButtonClick = { onDetailButtonClick(TermKind.PRIVACY) }
        )

        TermRow(
            checked = terms.location,
            label = "[필수] 위치 기반 서비스 이용 동의",
            kind = TermKind.LOCATION,
            onCheckboxButtonClick = { onCheckBoxButtonClick(TermKind.LOCATION) },
            onDetailButtonClick = { onDetailButtonClick(TermKind.LOCATION) }
        )
    }
}

@Composable
private fun TermRow(
    checked: Boolean,
    label: String,
    kind: TermKind,
    onCheckboxButtonClick: () -> Unit,
    onDetailButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onCheckboxButtonClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { _ -> onCheckboxButtonClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = CS.PrimaryOrange.O40,
                uncheckedColor = CS.Gray.G40
            )
        )
        Text(
            text = label,
            style = if (kind == TermKind.ALL) Typography.h3 else Typography.body1Regular,
            color = CS.Gray.G90,
            modifier = Modifier
                .weight(1f)
        )
        if (kind != TermKind.ALL) {
            Row(
                modifier = Modifier
                    .clickable(onClick = { onDetailButtonClick() }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("자세히", style = Typography.body2Regular, color = CS.Gray.G70)
                RightArrowIcon(14.dp, 14.dp, tint = CS.Gray.G50)
            }
        }
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