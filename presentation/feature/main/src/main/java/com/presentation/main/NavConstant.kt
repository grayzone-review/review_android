package com.presentation.main

import com.team.common.feature_api.navigation_constant.NavigationRouteConstant

internal object NavConstant {
    enum class Section(val value: String) {
        Popular("popular"),
        MyTown("my_town"),
        InterestRegions("interest_regions")
    }
    private const val MAIN_FEED_ADDRESS_BASE = NavigationRouteConstant.mainFeedSceneRoute
    private const val ARGUMENT_SECTION = "section"
    private const val ARGUMENT_MODE = "mode"
    const val MAIN_FEED_ROUTE =
        "$MAIN_FEED_ADDRESS_BASE?$ARGUMENT_SECTION={$ARGUMENT_SECTION}"

    fun destFeed(section: Section) =
        "$MAIN_FEED_ADDRESS_BASE?$ARGUMENT_SECTION=${section.value}"
}

internal sealed class MainNavRoute(val route: String) {
    data object MainFeed : MainNavRoute(NavConstant.MAIN_FEED_ROUTE)
}