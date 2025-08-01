package create_review_dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.domain.entity.CompactCompany
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.team.common.feature_api.error.APIException
import common_ui.AlertStyle
import common_ui.UpIndicator
import common_ui.UpSingleButtonAlertDialog
import create_review_dialog.CreateReviewDialogViewModel.Action.DidTapClearButton
import create_review_dialog.CreateReviewDialogViewModel.Action.DidTapNextButton
import create_review_dialog.CreateReviewDialogViewModel.Action.DidTapPreviousButton
import create_review_dialog.CreateReviewDialogViewModel.Action.DidTapSubmitButton
import create_review_dialog.CreateReviewDialogViewModel.Action.DidTapTextField
import create_review_dialog.CreateReviewDialogViewModel.Action.OnAppear
import create_review_dialog.CreateReviewDialogViewModel.Action.Reset
import create_review_dialog.CreateReviewDialogViewModel.Action.SheetDismissed
import create_review_dialog.CreateReviewDialogViewModel.Action.UpdateCompany
import create_review_dialog.CreateReviewDialogViewModel.Action.UpdateEmploymentPeriod
import create_review_dialog.CreateReviewDialogViewModel.Action.UpdateRatings
import create_review_dialog.CreateReviewDialogViewModel.Action.UpdateSearchQuery
import create_review_dialog.CreateReviewDialogViewModel.Action.UpdateTextFieldValue
import create_review_dialog.contents.FirstContent
import create_review_dialog.contents.SecondContent
import create_review_dialog.contents.ThirdContent
import create_review_dialog.sheet_contents.InputContainer
import create_review_dialog.sheet_contents.WorkPeriod
import create_review_dialog.type.CreateReviewPhase
import create_review_dialog.type.InputField
import create_review_dialog.type.sheetPartially
import create_review_dialog.type.step
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import preset_ui.icons.CloseLine

@Composable
fun CreateReviewDialog(
    company: CompactCompany? = null,
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = {
            viewModel.handleAction(Reset)
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        content(
            company = company,
            onDismiss = { viewModel.handleAction(Reset); onDismiss() },
            viewModel = viewModel
        )
    }
}

@Composable
private fun content(
    company: CompactCompany? = null,
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var alertError by remember { mutableStateOf<APIException?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    BackHandler {
        when (uiState.phase) {
            CreateReviewPhase.First -> { onDismiss() }
            CreateReviewPhase.Second -> { viewModel.handleAction(DidTapPreviousButton) }
            CreateReviewPhase.Third -> { viewModel.handleAction(DidTapPreviousButton) }
        }
    }

    LaunchedEffect(company != null) {
        viewModel.handleAction(OnAppear, company)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                CreateReviewUIEvent.DismissDialog -> { onDismiss() }
                is CreateReviewUIEvent.ShowAlert -> { alertError = event.error }
                is CreateReviewUIEvent.ShowSuccessAlert -> { successMessage = event.message }
            }
        }
    }

    BindSuccessAlert(
        message = successMessage,
        completion = {
            successMessage = null
            onDismiss()
        }
    )

    BindErrorAlert(
        error = alertError,
        completion = { alertError = null }
    )

    InputBottomSheet(
        uiState = uiState,
        bottomSheetState = uiState.bottomSheetState,
        onDismissedSheet = { viewModel.handleAction(SheetDismissed) },
        onSelectPeriodItem = { viewModel.handleAction(UpdateEmploymentPeriod, it) },
        onClickSaveButton = { field, text -> viewModel.handleAction(UpdateTextFieldValue, field to text) },
        onChangeSearchCompaniesQuery = { viewModel.handleAction(UpdateSearchQuery, it) },
        onCompanyItemClick = { viewModel.handleAction(UpdateCompany, it) },
        onClickClearButton = { viewModel.handleAction(DidTapClearButton) }
    )

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = CS.Gray.White)
        ) {
            DefaultTopAppBar(
                title = "리뷰작성",
                actions = {
                    IconButton(
                        onClick = { onDismiss() }
                    ) {
                        CloseLine(24.dp, 24.dp, tint = CS.Gray.G90)
                    }
                }
            )
            StepProgressBar(currentStep = uiState.phase.step, totalStep = 3)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                when (uiState.phase) {
                    CreateReviewPhase.First -> {
                        FirstContent(
                            uiState = uiState,
                            onCompanyClick = { viewModel.handleAction(DidTapTextField, InputField.Company) },
                            onJobRoleClick = { viewModel.handleAction(DidTapTextField, InputField.JobRole)},
                            onPeriodClick = { viewModel.handleAction(DidTapTextField, InputField.EmploymentPeriod) },
                        )
                    }

                    CreateReviewPhase.Second -> {
                        SecondContent(
                            uiState = uiState,
                            onCompanyNameClick = { viewModel.handleAction(DidTapTextField, InputField.Company) },
                            onRatingsChanged = { viewModel.handleAction(UpdateRatings, it) }
                        )
                    }

                    CreateReviewPhase.Third -> {
                        ThirdContent(
                            uiState = uiState,
                            onAdvantagePointClick = { viewModel.handleAction(DidTapTextField, InputField.Advantage)},
                            onDisadvantagePointClick = { viewModel.handleAction(DidTapTextField, InputField.Disadvantage) },
                            onManagementFeedBackClick = { viewModel.handleAction(DidTapTextField, InputField.ManagementFeedback) }
                        )
                    }
                }
            }
            ReviewDialogButtons(
                phase = uiState.phase,
                enabled = uiState.isNextAndSubmitEnabled,
                onClickNextButton = { viewModel.handleAction(DidTapNextButton) },
                onClickBackButton = { viewModel.handleAction(DidTapPreviousButton) },
                onClickSubmitButton = { viewModel.handleAction(DidTapSubmitButton) }
            )
        }

        UpIndicator(isShow = uiState.isLoading, needDim = uiState.isLoading)
    }

}

@Composable
private fun ReviewDialogButtons(
    phase: CreateReviewPhase,
    enabled: Boolean,
    onClickNextButton: () -> Unit,
    onClickBackButton: () -> Unit,
    onClickSubmitButton: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 5.dp),
    ) {
        val shape = RoundedCornerShape(8.dp)
        val nextAndSubmitColor = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White,
            disabledContainerColor = CS.PrimaryOrange.O20,
            disabledContentColor = CS.Gray.White
        )

        when (phase) {
            CreateReviewPhase.First -> {
                Button(
                    onClick = onClickNextButton,
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = shape,
                    colors = nextAndSubmitColor
                ) {
                    Text("다음", style = Typography.body1Bold, color = CS.Gray.White)
                }
            }

            CreateReviewPhase.Second -> {
                TwoButtonRow(
                    leftText = "이전",
                    rightText = "다음",
                    onClickLeft = onClickBackButton,
                    onClickRight = onClickNextButton,
                    rightEnabled = enabled
                )
            }

            CreateReviewPhase.Third -> {
                TwoButtonRow(
                    leftText = "이전",
                    rightText = "작성완료",
                    onClickLeft = onClickBackButton,
                    onClickRight = onClickSubmitButton,
                    rightEnabled = enabled
                )
            }
        }
    }
}

@Composable
fun TwoButtonRow(
    leftText: String,
    rightText: String,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    rightEnabled: Boolean = true
) {
    val shape = RoundedCornerShape(8.dp)
    val buttonHeight = 52.dp
    val nextAndSubmitColor = ButtonDefaults.buttonColors(
        containerColor = CS.PrimaryOrange.O40,
        contentColor = CS.Gray.White,
        disabledContainerColor = CS.PrimaryOrange.O20,
        disabledContentColor = CS.Gray.White
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onClickLeft,
            modifier = Modifier
                .weight(1f)
                .height(buttonHeight),
            shape   = shape,
            border  = BorderStroke(1.dp, CS.PrimaryOrange.O40),
            colors  = ButtonDefaults.buttonColors(containerColor = CS.Gray.White),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(text = leftText, style = Typography.body1Bold, color = CS.PrimaryOrange.O40)
        }

        Button(
            onClick = onClickRight,
            enabled = rightEnabled,
            modifier = Modifier
                .weight(1f)
                .height(buttonHeight),
            shape  = shape,
            colors = nextAndSubmitColor // disabled enabled 컬러 따르게함
        ) {
            Text(rightText, style = Typography.body1Bold)
        }
    }
}

@Composable
fun StepProgressBar(
    currentStep: Int,
    totalStep: Int,
    modifier: Modifier = Modifier
) {
    val progress = (currentStep.coerceAtLeast(0)
        .coerceAtMost(totalStep))
        .toFloat() / totalStep.coerceAtLeast(1)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "stepProgress"
    )

    val barHeight = 6.dp
    val startColor = Color(0xFFFF9573)
    val endColor   = Color(0xFFFF6E40)
    val background = CS.Gray.G10

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        /* ─── 프로그레스 바 ─────────────────────── */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(barHeight / 2))
                .background(background)
        ) {
            // 프로그레스 내부
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)   // 0f‒1f
                    .clip(RoundedCornerShape(barHeight / 2))
                    .background(
                        Brush
                            .horizontalGradient(listOf(startColor, endColor))
                    )
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(text = currentStep.toString(), style = Typography.captionBold, color = CS.PrimaryOrange.O40)
            Text(text = "/",                    style = Typography.captionBold, color = CS.Gray.G50)
            Text(text = totalStep.toString(),   style = Typography.captionBold, color = CS.Gray.G50)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBottomSheet(
    bottomSheetState: BottomSheetState,
    uiState: CreateReviewUIState,
    onDismissedSheet: () -> Unit,
    onSelectPeriodItem: (WorkPeriod) -> Unit,
    onClickSaveButton: (InputField, String) -> Unit,
    onChangeSearchCompaniesQuery: (String) -> Unit,
    onCompanyItemClick: (CompactCompany) -> Unit,
    onClickClearButton: () -> Unit
) {
    if (bottomSheetState !is BottomSheetState.Visible) return

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = bottomSheetState.field.inputType.sheetPartially
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismissedSheet()
                onClickClearButton() // 쿼리 제거 요청
            }
        },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.HiddenShape }
    ) {
        InputContainer(
            uiState = uiState,
            inputField = bottomSheetState.field,
            onSelectPeriodItem = {
                scope.launch {
                    onSelectPeriodItem(it)
                    delay(300)
                    sheetState.hide()
                    onDismissedSheet()
                }
            },
            onCloseButtonClick = {
                scope.launch {
                    sheetState.hide()
                    onDismissedSheet()
                }
            },
            onSaveButtonClick = { field, text ->
                scope.launch {
                    onClickSaveButton(field, text)
                    sheetState.hide()
                    onDismissedSheet()
                }
            },
            onChangeSearchCompaniesQuery = { onChangeSearchCompaniesQuery(it) },
            onCompanyItemClick = {
                scope.launch {
                    onCompanyItemClick(it)
                    delay(300)
                    sheetState.hide()
                    onClickClearButton()
                    onDismissedSheet()
                }
            },
            onClickClearButton = { onClickClearButton() }
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