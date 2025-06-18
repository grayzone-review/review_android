package com.presentation.main.scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import create_review_dialog.CreateReviewDialog

@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateReviewDialog(isShow = uiState.isShowCreateReviewDialog, onDismiss =  { })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {  },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "리뷰-생성")
        }
        
        Button(
            onClick = {  }
        ) {
            Text(text = "리뷰-검색")
        }
    }
}

@Composable
fun CreateReviewDialog(isShow: Boolean, onDismiss: () -> Unit) {
    if (isShow) {
        CreateReviewDialog(onDismiss = onDismiss)
    }
}