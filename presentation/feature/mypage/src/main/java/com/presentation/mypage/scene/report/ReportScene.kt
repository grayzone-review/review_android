package com.presentation.mypage.scene.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.mypage.scene.report.ReportViewModel.Action.OnAppear
import com.presentation.mypage.scene.report.ReportViewModel.Action.Submit
import com.presentation.mypage.scene.report.ReportViewModel.Action.UpdateDetailReason
import com.presentation.mypage.scene.report.ReportViewModel.Action.UpdateReportedUserNickName
import com.presentation.mypage.scene.report.ReportViewModel.Action.UpdateSelectReason
import com.presentation.mypage.scene.report.type.ReportReason
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.extension.addFocusCleaner
import common_ui.AlertStyle
import common_ui.UpSingleButtonAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import preset_ui.SimpleTextField
import preset_ui.SimpleTextFieldButton
import preset_ui.icons.BackBarButtonIcon
import preset_ui.icons.ReviewCheckLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScene(
    viewModel: ReportViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    var alertError by remember { mutableStateOf<APIException?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ReportUIEvent.ShowAlert -> { alertError = event.error }
                is ReportUIEvent.ShowSuccessAlert -> { successMessage = event.message }
            }
        }
    }

    ReportReasonModalSheet(
        sheetState = sheetState,
        selected = uiState.reason,
        onSelectReason = {
            scope.launch {
                viewModel.handleAction(UpdateSelectReason, it)
                delay(300)
                sheetState.hide()
            }
       },
        onCloseButtonClick = { scope.launch { sheetState.hide() } }
    )

    BindSuccessAlert(
        message = successMessage,
        completion = {
            successMessage = null
            navController.popBackStack()
        }
    )

    BindErrorAlert(
        error = alertError,
        completion = { alertError = null }
    )

    Scaffold(
        topBar = { TopAppBar(onBackButtonClick = { navController.popBackStack() }) },
        bottomBar = { SubmitButton(isEnabled = uiState.isSubmitEnabled, onClick = { viewModel.handleAction(Submit) }) },
        containerColor = CS.Gray.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .addFocusCleaner(focusManager = focusManager)
        ) {
            GuideBanner()
            ReporterField(
                value = uiState.user.nickname ?: "",
                onValueChange = { }
            )
            Spacer(modifier = Modifier.height(15.dp))
            ReportReasonSelectField(
                value = uiState.reason?.rawValue ?: "",
                onClick = { scope.launch { sheetState.expand() } }
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (uiState.reason != ReportReason.BUG) {
                ReportedUserNameField(
                    value = uiState.reportedUserNickname,
                    onValueChange = { viewModel.handleAction(UpdateReportedUserNickName, it) }
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
            ReportReasonDetailField(
                value = uiState.detailReason,
                onValueChange = { viewModel.handleAction(UpdateDetailReason, it) },
                placeholder = "신고 사유를 작성해주세요"
            )
        }
    }
}

@Composable
private fun TopAppBar(
    onBackButtonClick: () -> Unit
) {
    DefaultTopAppBar(
        title = "신고하기",
        leftNavigationIcon = {
            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90, modifier = Modifier
                .clickable { onBackButtonClick() })
        }
    )
}

@Composable
private fun GuideBanner() {
    Text(
        text = """
            UP은 보다 나은 커뮤니티 환경을 위해 지속적으로
            모니터링을 진행하고 있습니다.
            리뷰나 댓글 등에 유해한 내용이 포함되어 있다면,
            신고 대상의 닉네임과 신고 사유를
            함께 기재해 보내주세요.

            접수된 신고는 담당자가 확인한 후,
            해당 게시물에 대해 적절한 조치를 취하고 있습니다.
        """.trimIndent(),
        style = Typography.body1Regular,
        color = CS.Gray.G90,
        modifier = Modifier.padding(all = 20.dp)
    )
}

@Composable
private fun FieldTitle(
    text: String
) {
    Text(text = text, style = Typography.h3, color = CS.Gray.G90, modifier = Modifier
        .padding(top = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun ReporterField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        FieldTitle(text = "신고자")
        SimpleTextField(
            value = value,
            readOnly = true,
            placeholder = "",
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun ReportReasonSelectField(
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        FieldTitle(text = "신고 사유 선택")
        SimpleTextFieldButton(
            value = value,
            placeholder = "신고 사유를 선택해주세요",
            selectableMark = true,
            onClick = onClick
        )
    }
}

@Composable
private fun ReportedUserNameField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        FieldTitle(text = "신고 대상 닉네임")
        SimpleTextField(
            value = value,
            readOnly = false,
            placeholder = "신고 대상의 닉네임을 기입해주세요",
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun ReportReasonDetailField(
    value: String,
    onValueChange:(String)->Unit,
    placeholder: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        FieldTitle(text = "신고 사유")
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = CS.Gray.G20,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(all = 16.dp)
        ) { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        style = Typography.body1Regular,
                        color = CS.Gray.G50
                    )
                }
                innerTextField()
            }
        }
        Spacer(Modifier.height(20.dp))
    }
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
        Text(text = "신고하기", style = Typography.body1Bold)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportReasonModalSheet(
    sheetState: SheetState,
    selected: ReportReason?,
    onSelectReason: (ReportReason) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {},
            containerColor = CS.Gray.White
        ) {
            Text(
                text = "신고사유",
                modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 8.dp),
                style = Typography.body1Bold,
                color = CS.Gray.G90,
                textAlign = TextAlign.Center
            )
            ReportReasonContent(
                selected = selected,
                onSelectReason = { onSelectReason(it) },
                onCloseButtonClick = onCloseButtonClick
            )
        }
    }
}

@Composable
fun ReportReasonContent(
    selected: ReportReason?,
    onSelectReason: (ReportReason) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    val reportReasons = ReportReason.entries.toList()

    Column(Modifier.fillMaxWidth()) {
        reportReasons.forEach { reason ->
            val isSelected = reason == selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { onSelectReason(reason) }
                    .background(if (isSelected) CS.Gray.G10 else CS.Gray.White)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reason.rawValue,
                    style = if (isSelected) Typography.body1Bold else Typography.body1Regular,
                    color = if (isSelected) CS.PrimaryOrange.O40 else CS.Gray.G90
                )
                Spacer(Modifier.weight(1f))
                if (isSelected) {
                    ReviewCheckLine(20.dp, 20.dp, tint = CS.PrimaryOrange.O40)
                }
            }
        }

        Text(
            text = "닫기",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCloseButtonClick)
                .padding(vertical = 24.dp),
            style = Typography.body1Regular,
            color = CS.Gray.G70,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BindErrorAlert(
    error: APIException?,
    completion: () -> Unit
) {
    error?.let {
        UpSingleButtonAlertDialog(
            message = it.message,
            style = AlertStyle.Error,
            onDismiss = { completion() }
        )
    }
}

@Composable
fun BindSuccessAlert(
    message: String?,
    completion: () -> Unit
) {
    message?.let {
        UpSingleButtonAlertDialog(
            message = message,
            style = AlertStyle.Complete,
            onDismiss = { completion() }
        )
    }
}