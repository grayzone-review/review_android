package com.presentation.mypage.scene.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.DidTapMyPageMenu
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.DismissCreateReviewSheet
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.OnAppear
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.ShowCreateReviewSheet
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.UpAlertIconDialog
import create_review_dialog.CreateReviewDialog
import preset_ui.icons.InfoIcon
import preset_ui.icons.MyPageBell
import preset_ui.icons.MyPageLogOut
import preset_ui.icons.MyPagePen
import preset_ui.icons.MyPagePerson
import preset_ui.icons.MyPageSmile
import preset_ui.icons.MyPageWithdraw

@Composable
fun MyPageScene(
    viewModel: MyPageViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    var alertMenu by remember { mutableStateOf<MyPageMenu?>(null) }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is MyPageUIEvent.NavigateTo -> {
                    val route = when(event.menu) {
                        MyPageMenu.UPDATE_PROFILE -> NavigationRouteConstant.mypageModifyUserSceneRoute
                        MyPageMenu.REPORT -> NavigationRouteConstant.mypageReportSceneRoute
                        MyPageMenu.REVIEW_HISTORY -> NavigationRouteConstant.archiveNestedRoute
                        else -> return@collect
                    }
                    navController.navigate(route)
                }
                is MyPageUIEvent.ShowAlert -> {
                    alertMenu = event.menu
                }
            }
        }
    }

    CreateReviewSheet(
        isShow = uiState.shouldShowCreateReviewSheet,
        onDismiss = { viewModel.handleAction(DismissCreateReviewSheet) }
    )

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    alertMenu?.let { menu ->
        when (menu) {
            MyPageMenu.WITHDRAW -> WithDrawAlert(
                onConfirm = { /* TODO: 회원 탈퇴 후, Alert Dismiss */ },
                onCancel = { alertMenu = null }
            )
            MyPageMenu.LOGOUT -> LogOutAlert(
                onConfirm = { /* TODO: 로그 아웃, Alert Dismiss */ },
                onCancel = { alertMenu = null }
            )
            else->{}
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar(title = "마이페이지") },
        bottomBar = {
            UpBottomBar(
                current = UpTab.MyPage, // 현재 탭에 맞게 설정
                onTabSelected = { tab ->
                    when (tab) {
                        UpTab.Home -> {
                            navController.navigate(NavigationRouteConstant.mainNestedRoute) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        UpTab.MyPage -> {}
                    }
                },
                onAddButtonClick = { viewModel.handleAction(ShowCreateReviewSheet) }
            )
        },
        containerColor = CS.Gray.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            MyPageMenuList(
                nickname = uiState.user.nickname ?: "",
                onClick = { viewModel.handleAction(DidTapMyPageMenu, it) }
            )
        }
    }
}

@Composable
fun MyPageMenuList(
    nickname:String,
    onClick:(MyPageMenu)->Unit
) {
    val icons:Map<MyPageMenu,@Composable ()->Unit> = mapOf(
        MyPageMenu.UPDATE_PROFILE to { MyPagePerson(24.dp, 24.dp) },
        MyPageMenu.REPORT to { MyPageBell(24.dp, 24.dp) },
        MyPageMenu.REVIEW_HISTORY to { MyPagePen(24.dp, 24.dp) },
        MyPageMenu.WITHDRAW to { MyPageWithdraw(24.dp, 24.dp) },
        MyPageMenu.LOGOUT to { MyPageLogOut(24.dp, 24.dp) }
    )

    LazyColumn {
        item { NameRow(nickname) }

        item { Spacer(Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(color = CS.Gray.G10)) }

        item { MenuRow(MyPageMenu.UPDATE_PROFILE, icons, onClick) }
        item { MenuRow(MyPageMenu.REPORT,         icons, onClick) }

        item { Spacer(Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 20.dp)
            .background(color = CS.Gray.G20)) }

        item { MenuRow(MyPageMenu.REVIEW_HISTORY, icons, onClick) }

        item { Spacer(Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 20.dp)
            .background(color = CS.Gray.G20)) }

        item { MenuRow(MyPageMenu.WITHDRAW, icons, onClick) }
        item { MenuRow(MyPageMenu.LOGOUT,   icons, onClick) }
    }
}

@Composable
private fun MenuRow(
    menu: MyPageMenu,
    icons: Map<MyPageMenu, @Composable () -> Unit>,
    onClick: (MyPageMenu) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(menu) }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icons[menu]?.invoke()
        Spacer(Modifier.width(12.dp))
        Text(text = menu.title, style = Typography.body1Regular, color = CS.Gray.G90)
    }
}

@Composable
private fun NameRow(
    nickname:String,
    modifier:Modifier = Modifier
) {
    val gray = CS.Gray.G90
    val orange = CS.PrimaryOrange.O40

    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 20.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "안녕하세요!",
            style = Typography.h3Regular,
            color = gray
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "${nickname}님",
            style = Typography.h3,
            color = orange
        )
        Spacer(Modifier.width(4.dp))
        MyPageSmile(24.dp, 24.dp)
    }
}

@Composable
fun WithDrawAlert(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    UpAlertIconDialog(
        icon = { InfoIcon(44.dp, 44.dp, tint = CS.PrimaryOrange.O40) },
        title = "회원 탈퇴",
        message = """
        탈퇴 후, 현재 계정으로 작성한 글·댓글 등을
        수정하거나 삭제할 수 없습니다.
        지금 탈퇴하시겠습니까?
    """.trimIndent(),
        confirmText = "탈퇴하기",
        cancelText = "취소",
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}


@Composable
fun LogOutAlert(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    UpAlertIconDialog(
        icon = null,
        title = "로그아웃",
        message = "로그아웃 하시겠습니까?",
        confirmText = "로그아웃",
        cancelText = "취소",
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}

@Composable
private fun CreateReviewSheet(isShow: Boolean, onDismiss: () -> Unit) {
    if (isShow) { CreateReviewDialog(onDismiss = onDismiss) }
}