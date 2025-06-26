package com.presentation.login.scenes.search_address

import address_finder.AddressFinder
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun SearchAddressScene(
    searchAddressViewModel: SearchAddressViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    AddressFinder()
}