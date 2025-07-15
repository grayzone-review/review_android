package com.presentation.mypage.scene.modify_user

import address_finder.AddressFinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.presentation.design_system.appbar.appbars.SearchableTopAppBar
import com.presentation.mypage.scene.modify_user.ModifySearchAddressViewModel.Action.UpdateQueryFromLocation
import com.presentation.mypage.scene.modify_user.ModifySearchAddressViewModel.Action.UpdateQueryFromSearching
import com.team.common.feature_api.extension.addFocusCleaner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ModifySearchAddressScene(
    viewModel: ModifySearchAddressViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager = LocalFocusManager.current)
            .background(CS.Gray.White)
    ) {
        SearchableTopAppBar(
            keyword = uiState.query,
            onKeywordChange = { viewModel.handleAction(UpdateQueryFromSearching, it) },
            onBack = { navController.popBackStack() },
            onCancel = { }
        )
        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(color = CS.Gray.G10))
        AddressFinder(
            query = uiState.query,
            onFindMyLocationButtonClick = { viewModel.handleAction(UpdateQueryFromLocation, it) },
            onAddressItemClick = {
                scope.launch {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("selectedLegalDistrictInfo", it)
                        set("selectedMode", uiState.mode)
                    }
                    delay(timeMillis = 300)
                    navController.popBackStack()
                }
            }
        )
    }
}