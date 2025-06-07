package com.feature.comments.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.presentation.design_system.appbar.appbars.AppBarAction
import com.presentation.design_system.appbar.appbars.AppBarState
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import java.nio.file.WatchEvent.Modifier

@Composable
fun SearchScene(
    viewModel: SearchViewModel,
    appBarViewModel: AppBarViewModel
) {
    LaunchedEffect(Unit) {
        appBarViewModel.updateAppBarState(
            AppBarState(
                title = "리뷰 상세",
                showBackButton = true,
                actions = listOf(
                    AppBarAction(
                        icon = null,
                        contentDescription = "수정",
                        onClick = { /* 수정 액션 */ }
                    )
                )
            )
        )
    }
    Column(
    ) {
        Text("안녕 난 서치씬")
    }
}