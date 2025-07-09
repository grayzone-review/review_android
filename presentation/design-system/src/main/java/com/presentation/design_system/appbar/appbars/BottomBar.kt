package com.presentation.design_system.appbar.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.BottomBarAddIcon
import preset_ui.icons.BottomBarHomeIcon
import preset_ui.icons.BottomBarMyIcon

enum class UpTab { Home, MyPage }

@Composable
fun UpBottomBar(
    current: UpTab,
    onTabSelected: (UpTab) -> Unit,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .graphicsLayer {
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    clip = true
                    shadowElevation = 4.dp.toPx()
                    val deepShadow = Color.Black.copy(alpha = 0.9f)   // 80 % 불투명
                    ambientShadowColor = deepShadow
                    spotShadowColor = deepShadow
                }
                /* 2️⃣ 잘린 내부를 흰색으로 칠해 삼각 영역까지 커버 */
                .background(CS.Gray.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomTab(
                    icon = { BottomBarHomeIcon(state = current == UpTab.Home, 24.dp, 24.dp) },
                    label = "홈",
                    selected = current == UpTab.Home,
                    onClick = { onTabSelected(UpTab.Home) }
                )
                BottomTab(
                    icon = { BottomBarMyIcon(state = current == UpTab.MyPage, 24.dp, 24.dp) },
                    label = "마이페이지",
                    selected = current == UpTab.MyPage,
                    onClick = { onTabSelected(UpTab.MyPage) }
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
        ) {
            FloatingActionButton(
                onClick = onAddButtonClick,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                BottomBarAddIcon(width = 56.dp, height = 56.dp)
            }
        }
    }
}

@Composable
private fun BottomTab(
    icon: @Composable ()->Unit,
    label:String,
    selected:Boolean,
    onClick:()->Unit
) {
    val labelColor = if(selected) CS.PrimaryOrange.O40 else CS.Gray.G50

    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Text(text = label, color = labelColor, style = Typography.captionRegular)
    }
}
