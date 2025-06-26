package address_finder

import address_finder.AddressFinderViewModel.Action.GetAddresses
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.MapPinTintable

@Composable
fun AddressFinder(
    query: String,
    viewModel: AddressFinderViewModel = hiltViewModel()
) {
    LaunchedEffect(query) {
        Log.d("여기 몇번타나", "몇번")
        viewModel.handleAction(GetAddresses, query)
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FindMyLocationButton(
            onClick = { }
        )
        AddressList(
            addresses = uiState.addresses
        )
    }
}

@Composable
fun AddressList(
    addresses: List<String>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(addresses) { address ->
            AddressBulletItem(address)
        }
    }
}

@Composable
fun AddressBulletItem(
    address: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ● 또는 • 원하는 글리프 사용
        Text(
            text = "•",
            style = Typography.body1Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = address,
            style = Typography.body1Regular
        )
    }
}


@Composable
private fun FindMyLocationButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MapPinTintable(16.dp, 16.dp, tint = CS.Gray.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "내 위치 찾기", style = Typography.body1Bold)
        }
    }
}