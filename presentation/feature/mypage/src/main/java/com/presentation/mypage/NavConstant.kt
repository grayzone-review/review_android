package com.presentation.mypage

import android.net.Uri
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant

internal object NavConstant {
    enum class Mode(val value: String) { MY("my"), INTEREST("interest") }
    private const val MODIFY_SEARCH_ADDRESS_BASE = NavigationRouteConstant.mypageModifySearchAddressSceneRoute
    private const val ARGUMENT_TOWN = "town"
    private const val ARGUMENT_MODE = "mode"
    const val SEARCH_ADDRESS_ROUTE =
        "$MODIFY_SEARCH_ADDRESS_BASE?$ARGUMENT_TOWN={$ARGUMENT_TOWN}&$ARGUMENT_MODE={$ARGUMENT_MODE}"

    fun destSearchAddress(startQuery: String, mode: Mode) =
        "$MODIFY_SEARCH_ADDRESS_BASE?$ARGUMENT_TOWN=${Uri.encode(startQuery)}&$ARGUMENT_MODE=${mode.value}"
}

internal sealed class MyPageNavRoute(val route: String) {
    data object SearchAddress : MyPageNavRoute(NavConstant.SEARCH_ADDRESS_ROUTE)
}