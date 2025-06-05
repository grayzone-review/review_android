package com.presentation.design_system.appbar.appbars

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor() : ViewModel() {
    private val _appBarState = MutableStateFlow(AppBarState())
    val appBarState: StateFlow<AppBarState> = _appBarState.asStateFlow()

    fun updateAppBarState(newState: AppBarState) {
        _appBarState.value = newState
    }
}

data class AppBarState(
    val title: String = "",
    val showBackButton: Boolean = false,
    val actions: List<AppBarAction> = emptyList()
)

data class AppBarAction(
    val icon: ImageVector? = null,
    val contentDescription: String,
    val onClick: () -> Unit
)