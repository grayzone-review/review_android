package com.presentation.design_system.appbar.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.BackBarButtonIcon
import preset_ui.icons.CloseFillIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, color = CS.Gray.G90, style = Typography.h2) },
        navigationIcon = { navigationIcon?.invoke() },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CS.Gray.White,
            titleContentColor = CS.Gray.G90
        ),
        modifier = modifier
    )
}

@Composable
fun SearchableTopAppBar(
    keyword: String,
    onKeywordChange: (String) -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isFocused) { BackBarButtonIcon(24.dp, 24.dp, tint = CS.Gray.G90, modifier = Modifier
            .clickable { onBack() }
            .padding(start = 10.dp, end = 14.dp))
        }

        BasicTextField(
            value = keyword,
            onValueChange = onKeywordChange,
            singleLine = true,
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CS.Gray.G10)
                .padding(horizontal = 16.dp)
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.weight(1f)) {
                        if (keyword.isEmpty()) {
                            Text(
                                "동명 (읍, 면)으로 검색 (ex. 서초동)",
                                style = Typography.body1Regular,
                                color = CS.Gray.G50
                            )
                        }
                        innerTextField()
                    }

                    if (isFocused) {
                        CloseFillIcon(24.dp, 24.dp, modifier = Modifier
                            .clickable { onKeywordChange("") }
                        )
                    }
                }
            }
        )

        if (isFocused) {
            Spacer(Modifier.width(8.dp))
            Text(text = "취소", style = Typography.body1Regular, color = CS.Gray.G90, modifier = Modifier
                .clickable {
                    focusManager.clearFocus()
                    onCancel()
                }
            )
        }
    }
}
