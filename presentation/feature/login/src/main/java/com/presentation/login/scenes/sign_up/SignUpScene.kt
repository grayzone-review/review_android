package com.presentation.login.scenes.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.LegalDistrictInfo
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.AddInterestTown
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.DidTapCheckBox
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.DidTapCheckDuplicateButton
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.DidTapDetailButton
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.DidTapRemoveInterestTownButton
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.DidTapSubmitButton
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.GetTerms
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.SetMyTown
import com.presentation.login.scenes.sign_up.SignUpViewModel.Action.UpdateNickNameTextField
import com.presentation.login.scenes.sign_up.navgraph.NavConstant
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.extension.addFocusCleaner
import common_ui.AlertStyle
import common_ui.UpSingleButtonAlertDialog
import edit_profile_address_component.InterestInput
import edit_profile_address_component.MyTownInput
import edit_profile_address_component.NicknameInput
import preset_ui.icons.CheckBoxIcon
import preset_ui.icons.CloseLine
import preset_ui.icons.RightArrowIcon

@Composable
fun SignUpScene(
    onDismiss: () -> Unit,
    onSubmitCompleted: () -> Unit,
    navHostController: NavHostController,
    accessToken: String,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var alertError by remember { mutableStateOf<APIException?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val savedStateHandle = navHostController.currentBackStackEntry!!.savedStateHandle
    val selectedAddress by savedStateHandle
        .getStateFlow<LegalDistrictInfo?>("selectedLegalDistrictInfo", null)
        .collectAsState()
    val mode by savedStateHandle
        .getStateFlow<String?>("selectedMode", null)
        .collectAsState()

    LaunchedEffect(accessToken) {
        viewModel.handleAction(SignUpViewModel.Action.SetAccessToken, accessToken)
    }
    LaunchedEffect(uiState.terms.isEmpty()) {
        if (uiState.terms.isEmpty()) {
            viewModel.handleAction(GetTerms)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SignUpUIEvent.ShowAlert -> { alertError = event.error }
                is SignUpUIEvent.ShowSuccessAlert -> { successMessage = event.message }
            }
        }
    }

    BindSuccessAlert(
        message = successMessage,
        completion = {
            successMessage = null
            onSubmitCompleted()
        }
    )

    BindErrorAlert(
        error = alertError,
        completion = { alertError = null }
    )

    LaunchedEffect(selectedAddress, mode) {
        val selectedLegalDistrict = selectedAddress ?: return@LaunchedEffect
        val mode= mode ?: return@LaunchedEffect

        when (mode) {
            NavConstant.Mode.MY.value -> viewModel.handleAction(SetMyTown, selectedLegalDistrict)
            NavConstant.Mode.INTEREST.value -> viewModel.handleAction(AddInterestTown, selectedLegalDistrict)
        }
        savedStateHandle["selectedLegalDistrictInfo"] = null
        savedStateHandle["selectedMode"] = null
    }

    Column(
        modifier = Modifier
            .background(CS.Gray.White)
            .addFocusCleaner(focusManager)
    ) {
        TopAppBar(onCloseButtonClick = { onDismiss() })
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            NicknameInput(
                nicknameField = uiState.nickNameField,
                onValueChange = { viewModel.handleAction(UpdateNickNameTextField, it) },
                onCheckDuplicate = { viewModel.handleAction(DidTapCheckDuplicateButton) }
            )
            MyTownInput(
                value = uiState.myTown,
                onClick = { navHostController.navigate(
                    NavConstant.destSearchAddress(startQuery = it, mode = NavConstant.Mode.MY))
                }
            )
            InterestInput(
                legalDistrictInfos = uiState.interestTowns,
                onRemoveButtonClick = { viewModel.handleAction(DidTapRemoveInterestTownButton, it) },
                onAddTownButtonClick = { navHostController.navigate(
                    NavConstant.destSearchAddress(startQuery = "", mode = NavConstant.Mode.INTEREST))
                }
            )
            TermsSection(
                terms = uiState.terms,
                onCheckBoxClick = { viewModel.handleAction(DidTapCheckBox, it) },
                onDetailClick = { viewModel.handleAction(DidTapDetailButton, it) },
            )
        }
        SubmitButton(
            isEnabled = uiState.isSubmitEnabled,
            onClick = { viewModel.handleAction(DidTapSubmitButton) }
        )
    }
}


enum class TermCode { ALL, SERVICE, PRIVACY, LOCATION }
@Composable
fun TermsSection(
    terms: List<TermCheck>,
    onCheckBoxClick: (TermCode) -> Unit,
    onDetailClick: (TermCode) -> Unit,
) {
    val allChecked = terms.filter { it.info.required }.all { it.isChecked }

    Column(Modifier.padding(horizontal = 20.dp)) {
        /* ── 전체 동의 ── */
        TermRow(
            checked = allChecked,
            label = "약관 전체 동의",
            code = TermCode.ALL,
            onCheckboxClick = { onCheckBoxClick(TermCode.ALL) },
            onDetailClick = {}
        )

        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = CS.Gray.G20
        )

        terms.forEach { term ->
            TermRow(
                checked = term.isChecked,
                label = term.info.term,
                code = when (term.info.code) {
                    "serviceUse" -> TermCode.SERVICE
                    "privacy"    -> TermCode.PRIVACY
                    "location"   -> TermCode.LOCATION
                    else         -> TermCode.SERVICE
                },
                onCheckboxClick = { code -> onCheckBoxClick(code) },
                onDetailClick   = { code -> onDetailClick(code) }
            )
        }
    }
}

@Composable
private fun TermRow(
    checked: Boolean,
    label: String,
    code: TermCode,
    onCheckboxClick: (TermCode) -> Unit,
    onDetailClick: (TermCode) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onCheckboxClick(code) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckBoxIcon(
            state = checked, width  = 24.dp, height = 24.dp,
            modifier = Modifier
                .padding(end = 8.dp)
        )

        Text(
            text = label,
            style = if (code == TermCode.ALL) Typography.body1Semi else Typography.body1Regular,
            color = CS.Gray.G90,
            modifier = Modifier.weight(1f)
        )
        if (code != TermCode.ALL) {
            Row(
                Modifier.clickable { onDetailClick(code) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("자세히", style = Typography.body2Regular, color = CS.Gray.G70)
                RightArrowIcon(14.dp, 14.dp, tint = CS.Gray.G50)
            }
        }
    }
}

@Composable
private fun TopAppBar(
    onCloseButtonClick: () -> Unit
) {
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