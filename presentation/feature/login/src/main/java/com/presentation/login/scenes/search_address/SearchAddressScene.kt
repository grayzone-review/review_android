package com.presentation.login.scenes.search_address

import address_finder.AddressFinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.presentation.design_system.appbar.appbars.SearchableTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchAddressScene(
    searchAddressViewModel: SearchAddressViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CS.Gray.White)
    ) {
        SearchableTopAppBar(
            keyword = "",
            onKeywordChange = { },
            onBack = { navHostController.popBackStack() },
            onCancel = { }
        )
        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(color = CS.Gray.G10))
        AddressFinder(
            query = "",
            onAddressItemClick = {
                scope.launch {
                    delay(timeMillis = 300)
                    navHostController.popBackStack()
                }
            }
        )
    }
}