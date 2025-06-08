package com.feature.comments.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import com.feature.comments.scene.SearchViewModel.Action.*
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.AppBarState
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import preset_ui.icons.CloseFillIcon
import preset_ui.icons.SearchLineIcon

@Composable
fun SearchScene(
    viewModel: SearchViewModel,
    appBarViewModel: AppBarViewModel
) {
    val searchUIState by viewModel.searchUIState.collectAsState()

    LaunchedEffect(Unit) {
        appBarViewModel.updateAppBarState(
            AppBarState(
                title = "업체 검색",
                showBackButton = true,
                actions = emptyList()
            )
        )
    }

    Column(Modifier
        .fillMaxSize()
    ) {
        SearchTextField(
            searchUIState = searchUIState,
            onTextChange = { viewModel.handleAction(DidUpdateSearchBarValue, text = it) },
            onFocusChange = { if (it.isFocused) viewModel.handleAction(DidFocusSearchBar) }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SearchTextField(
    searchUIState: SearchUIState,
    onTextChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    val borderColor = if (searchUIState.hasFocus) CS.Gray.G90 else CS.Gray.G20

    Row(
        modifier = Modifier
            .height(84.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(Color.Blue),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchUIState.searchBarValue.text,
            onValueChange = onTextChange,
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(shape)
                .border(1.dp, color = borderColor, shape = shape)
                .onFocusChanged { onFocusChange(it) },
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            decorationBox = searchDecorationBox(searchUIState = searchUIState),
            singleLine = true,
            maxLines = 1
        )
    }
}

@Composable
fun searchDecorationBox(
    searchUIState: SearchUIState
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { inner ->
    val shape = RoundedCornerShape(8.dp)
    val isEmptyTextFieldValue = searchUIState.searchBarValue.text.isEmpty()

    Box(
        modifier = Modifier
            .background(Color.White, shape)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchLineIcon(24.dp, 24.dp)

            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                // 2. Placeholder
                if (isEmptyTextFieldValue) {
                    Text(
                        text = "상호명으로 검색하기",
                        style = Typography.body1Regular,
                        color = CS.Gray.G50
                    )
                }

                inner()
            }

            if (!isEmptyTextFieldValue) {
                CloseFillIcon(24.dp, 24.dp)
            }
        }
    }
}
