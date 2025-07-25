package com.presentation.mypage.scene.modify_user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.LegalDistrictInfo
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.mypage.NavConstant
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.AddInterestTown
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.DidTapCheckDuplicateButton
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.DidTapRemoveInterestTownButton
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.DidTapSubmitButton
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.OnAppear
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.SetMyTown
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel.Action.UpdateNickNameTextField
import com.team.common.feature_api.extension.addFocusCleaner
import edit_profile_address_component.InterestInput
import edit_profile_address_component.MyTownInput
import edit_profile_address_component.NicknameInput
import preset_ui.icons.BackBarButtonIcon

@Composable
fun ModifyUserScene(
    viewModel: ModifyUserViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val savedStateHandle = navController.currentBackStackEntry!!.savedStateHandle
    val selectedAddress by savedStateHandle
        .getStateFlow<LegalDistrictInfo?>("selectedLegalDistrictInfo", null)
        .collectAsState()
    val mode by savedStateHandle
        .getStateFlow<String?>("selectedMode", null)
        .collectAsState()

//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.presentation.design_system.R.raw.insider_loading))
//
//    /* iterations = LottieConstants.IterateForever 로 무한 반복 */
//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        iterations = LottieConstants.IterateForever,    // ★ 핵심
//        isPlaying = true,                               // 기본값 true 지만 명시해 두면 헷갈리지 않음
//        speed = 1f
//    )

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                ModifyUserUIEvent.Pop -> { navController.popBackStack() }
            }
        }
    }

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

    Box(
    ) {
        Column(
            Modifier
                .background(CS.Gray.White)
                .addFocusCleaner(focusManager = focusManager)
        ) {
            TopAppBar(onBackButtonClick = { navController.popBackStack() })
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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
                    onClick = { navController.navigate(NavConstant
                        .destSearchAddress(startQuery = it, mode = NavConstant.Mode.MY))
                    }
                )
                InterestInput(
                    legalDistrictInfos = uiState.interestTowns,
                    onRemoveButtonClick = { viewModel.handleAction(DidTapRemoveInterestTownButton, it) },
                    onAddTownButtonClick = { navController.navigate(NavConstant
                        .destSearchAddress(startQuery = "", mode = NavConstant.Mode.INTEREST))
                    }
                )
            }
            SubmitButton(
                isEnabled = uiState.isSubmitEnabled,
                onClick = { viewModel.handleAction(DidTapSubmitButton) }
            )
        }

//        LottieAnimation(
//            composition = composition,
//            progress = { progress },
//            modifier = Modifier.fillMaxSize()
//                .align(Alignment.Center)
//        )
    }
}

@Composable
private fun TopAppBar(
    onBackButtonClick: () -> Unit
) {
    DefaultTopAppBar(
        title = "내 정보 수정",
        leftNavigationIcon = {
            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90, modifier = Modifier
                .clickable { onBackButtonClick() })
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
        Text(text = "수정하기", style = Typography.body1Bold)
    }
}