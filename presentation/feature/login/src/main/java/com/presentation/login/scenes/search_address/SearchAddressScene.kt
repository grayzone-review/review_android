package com.presentation.login.scenes.search_address

import address_finder.AddressFinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS

@Composable
fun SearchAddressScene(
    searchAddressViewModel: SearchAddressViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CS.Gray.White)
    ) {
        AddressFinder(query = "")
    }
}