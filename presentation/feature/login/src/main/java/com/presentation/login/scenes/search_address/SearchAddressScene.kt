package com.presentation.login.scenes.search_address

import address_finder.AddressFinder
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchAddressScene(
    searchAddressViewModel: SearchAddressViewModel = hiltViewModel()
) {
    AddressFinder()
}