package address_finder

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddressFinder(
    viewModel: AddressFinderViewModel = hiltViewModel()
) {
    Text("안녕 난 어드파인더")
}