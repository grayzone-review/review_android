package com.presentation.mypage.scene.modify_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS

@Composable
fun ModifyUserScene(
    viewModel: ModifyUserViewModel = hiltViewModel(),
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .background(CS.Gray.White)
    ) {

    }
}

@Composable
fun abc() {

}