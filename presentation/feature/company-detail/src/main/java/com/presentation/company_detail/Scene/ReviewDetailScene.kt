package com.presentation.company_detail.Scene

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.presentation.design_system.appbar.appbar.AppBarAction
import com.presentation.design_system.appbar.appbar.AppBarState
import com.presentation.design_system.appbar.appbar.AppBarViewModel

@Composable
fun ReviewDetailScene(
    viewModel: ReviewDetailViewModel,
    appBarViewModel: AppBarViewModel  // 추가
) {
    // 화면 진입 시 AppBar 상태 변경
    LaunchedEffect(Unit) {
        appBarViewModel.updateAppBarState(
            AppBarState(
                title = "리뷰 상세",
                showBackButton = true,
                actions = listOf(
                    AppBarAction(
                        icon = Icons.Default.Edit,
                        contentDescription = "수정",
                        onClick = { /* 수정 액션 */ }
                    )
                )
            )
        )
    }

    // 기존 화면 내용
    val result = viewModel.reviewDetailTestText
    Text(text = result)
}
