package com.presentation.mypage.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant

@Composable
fun MyPageScene(
    viewModel: MyPageViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = {
            UpBottomBar(
                current = UpTab.MyPage, // 현재 탭에 맞게 설정
                onTabSelected = {
                    navController.navigate(NavigationRouteConstant.mainNestedRoute)
                },
                onAddButtonClick = { /* TODO */ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}