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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavHostController
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.DidTapMyPageMenu
import com.presentation.mypage.scene.mypage.MyPageViewModel.Action.GetUser
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
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
                        MyPageMenu.REPORT -> "report"
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

    LaunchedEffect(Unit) {
        viewModel.handleAction(GetUser)
    }

    alertMenu?.let { menu ->
        ActionConfirmDialog(
            menu = menu,
            onConfirm = {
                alertMenu = null
            },
            onDismiss = { alertMenu = null }
        )
    }

    Scaffold(
        topBar = { DefaultTopAppBar(title = "마이페이지") },
        bottomBar = {
            UpBottomBar(
                current = UpTab.MyPage, // 현재 탭에 맞게 설정
                onTabSelected = {
                    navController.navigate(NavigationRouteConstant.mainNestedRoute)
                },
                onAddButtonClick = { /* TODO */ }
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

        item { Spacer(Modifier.fillMaxWidth().height(8.dp).background(color = CS.Gray.G10)) }

        item { MenuRow(MyPageMenu.UPDATE_PROFILE, icons, onClick) }
        item { MenuRow(MyPageMenu.REPORT,         icons, onClick) }

        item { Spacer(Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 20.dp).background(color = CS.Gray.G20)) }

        item { MenuRow(MyPageMenu.REVIEW_HISTORY, icons, onClick) }

        item { Spacer(Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 20.dp).background(color = CS.Gray.G20)) }

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
        Text(text = menu.title, style = Typography.h3, color = CS.Gray.G90)
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
private fun ActionConfirmDialog(
    menu: MyPageMenu,
    onConfirm:()->Unit,
    onDismiss:()->Unit
) {
    val isWithdraw = menu == MyPageMenu.WITHDRAW
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        title = { Text(if(isWithdraw) "회원 탈퇴" else "로그아웃") },
        text = {
            Text(
                if(isWithdraw)
                    "회원 탈퇴하시겠습니까?\n작성한 글과 댓글은 삭제되지 않습니다."
                else
                    "로그아웃하시겠습니까?"
            )
        }
    )
}