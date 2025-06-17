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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import com.feature.comments.scene.SearchViewModel.SearchInterfaceAction.*
import com.feature.comments.scene.SearchViewModel.ContentAction.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.feature.comments.scene.contents.AfterContent
import com.feature.comments.scene.contents.BeforeContent
import com.feature.comments.scene.contents.SearchingContent
import com.presentation.design_system.appbar.appbars.AppBarState
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.team.common.feature_api.extension.addFocusCleaner
import preset_ui.icons.CloseFillIcon
import preset_ui.icons.SearchLineIcon
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.domain.entity.Company
import com.domain.entity.SearchedCompany
import com.feature.comments.scene.contents.AfterContentViewModel
import com.feature.comments.scene.contents.BeforeContentViewModel
import com.feature.comments.scene.contents.SearchingContentViewModel
import com.feature.comments.scene.contents.TagButtonData
import com.feature.comments.scene.contents.TagButtonType
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant

@Composable
fun SearchScene(
    viewModel: SearchViewModel,
    appBarViewModel: AppBarViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    val searchUIState by viewModel.searchUIState.collectAsState()

    Appbar(appBarViewModel = appBarViewModel, searchUIState = searchUIState)
    Column(Modifier
        .fillMaxSize()
        .addFocusCleaner(focusManager = focusManager)
    ) {
        SearchTextField(
            searchUIState = searchUIState,
            onTextChange = { viewModel.handleAction(DidUpdateSearchBarValue, text = it) },
            onFocusChange = { if (it.isFocused) viewModel.handleAction(DidFocusSearchBar) else viewModel.handleAction(DidUnfocusSearchBar) },
            onClickClearButton = { viewModel.handleAction(DidTapClearButton) },
            onClickCancelButton = {
                focusManager.clearFocus()
                viewModel.handleAction(DidTapCancelButton)
            },
            onClickIMEDone = {
                focusManager.clearFocus()
                viewModel.handleAction(DidTapIMEDone)
            }
        )
        Content(
            searchUIState = searchUIState,
            onClickRecentQuery = { viewModel.handleAction(DidTapRecentQueryButton, text = it) },
            onClickFilterButton = { viewModel.handleAction(DidTapFilterButtons, tagButtonData = it) },
            onClickRecentCompany = { company -> 
                navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute.replace("{companyId}", company.id.toString()))
            },
            onClickSearchedCompany = { company -> 
                navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute.replace("{companyId}", company.id.toString()))
            },
            onClickSearchResultCompany = { company ->
                navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute.replace("{companyId}", company.id.toString()))
            }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Appbar(appBarViewModel: AppBarViewModel, searchUIState: SearchUIState) {
    val phase = searchUIState.phase

    LaunchedEffect(phase) {
        when (phase) {
            SearchPhase.Before -> {
                appBarViewModel.updateAppBarState(
                    AppBarState(
                        title = "업체 검색",
                        showBackButton = true,
                        actions = emptyList(),
                        isVisible = true
                    )
                )
            }
            SearchPhase.Searching, SearchPhase.After -> {
                appBarViewModel.updateAppBarState(
                    AppBarState(isVisible = false)
                )
            }
        }
    }
}

@Composable
fun SearchTextField(
    searchUIState: SearchUIState,
    onTextChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    onClickCancelButton: () -> Unit,
    onClickClearButton: () -> Unit,
    onClickIMEDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    val borderColor = if (searchUIState.hasFocus) CS.Gray.G90 else CS.Gray.G20

    Row(
        modifier = Modifier
            .height(84.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchUIState.searchBarValue.text,
            onValueChange = onTextChange,
            modifier = modifier
                .weight(1f)
                .height(52.dp)
                .clip(shape)
                .border(1.dp, color = borderColor, shape = shape)
                .onFocusChanged { onFocusChange(it) },
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onClickIMEDone() }),
            decorationBox = searchDecorationBox(
                searchUIState = searchUIState,
                onClickClearButton = onClickClearButton
            ),
            singleLine = true,
            maxLines = 1
        )


        if (searchUIState.phase != SearchPhase.Before) {
            TextButton(onClick = onClickCancelButton) {
                Text(text = "취소", style = Typography.body1Regular, color = CS.Gray.G90)
            }
        }
    }
}

@Composable
fun searchDecorationBox(
    searchUIState: SearchUIState,
    onClickClearButton: () -> Unit
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
            SearchLineIcon(24.dp, 24.dp, tint = CS.Gray.G90)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                // 2. Placeholder
                if (isEmptyTextFieldValue) {
                    Text(text = "상호명으로 검색하기", style = Typography.body1Regular, color = CS.Gray.G50)
                }

                inner()
            }
            if (!isEmptyTextFieldValue) {
                IconButton(onClick = onClickClearButton) {
                    CloseFillIcon(width = 24.dp, height = 24.dp)
                }
            }
        }
    }
}

@Composable
fun Content(
    searchUIState: SearchUIState,
    onClickRecentQuery: (String) -> Unit,
    onClickFilterButton: (TagButtonData) -> Unit,
    onClickRecentCompany: (Company) -> Unit,
    onClickSearchedCompany: (SearchedCompany) -> Unit,
    onClickSearchResultCompany: (SearchedCompany) -> Unit
) {
    when (searchUIState.phase) {
        SearchPhase.Before -> { 
            val viewModel: BeforeContentViewModel = hiltViewModel()
            BeforeContent(
                viewModel = viewModel,
                onClickTag = { onClickRecentQuery(it) },
                onClickFilterButton = { onClickFilterButton(it) }
            )
        }
        SearchPhase.Searching -> { 
            val viewModel: SearchingContentViewModel = hiltViewModel()
            SearchingContent(
                viewModel = viewModel,
                searchUIState = searchUIState,
                onRecentCompanyClick = { onClickRecentCompany(it) },
                onSearchedCompanyClick = { onClickSearchedCompany(it) }
            ) 
        }
        SearchPhase.After -> { 
            val viewModel: AfterContentViewModel = hiltViewModel()
            AfterContent(
                viewModel = viewModel,
                searchUIState = searchUIState,
                onClickSearchResult = onClickSearchResultCompany
            )
        }
    }
}