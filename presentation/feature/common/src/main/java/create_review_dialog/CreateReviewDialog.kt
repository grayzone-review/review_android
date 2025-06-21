package create_review_dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.domain.entity.SearchedCompany
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import create_review_dialog.contents.FirstContent
import create_review_dialog.contents.SecondContent
import create_review_dialog.contents.ThirdContent
import create_review_dialog.CreateReviewDialogViewModel.Action.*
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
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        content(onDismiss = onDismiss, viewModel = viewModel)
    }
}

@Composable
private fun content(
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

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
    onCompanyItemClick: (SearchedCompany) -> Unit,
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