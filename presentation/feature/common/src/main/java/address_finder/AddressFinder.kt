package address_finder

import address_finder.AddressFinderViewModel.Action.DismissSettingAlert
import address_finder.AddressFinderViewModel.Action.ShoudShowSettingAlert
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.data.location.LocationService
import com.example.presentation.designsystem.typography.Typography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.team.common.feature_api.extension.openAppSettings
import preset_ui.icons.CheckCircleFill
import preset_ui.icons.MapPinTintable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddressFinder(
    query: String,
    viewModel: AddressFinderViewModel = hiltViewModel(),
    onAddressItemClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val permissionState = rememberMultiplePermissionsState(LocationService.locationPermissions)
    val context = LocalContext.current

    LaunchedEffect(query) { viewModel.handleAction(AddressFinderViewModel.Action.GetAddresses, query) }

    SettingAlertDialog(
        isShow = uiState.shouldShowSettingAlert,
        onDismiss = { viewModel.handleAction(DismissSettingAlert) },
        onOpenSettings = { context.openAppSettings() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FindMyLocationButton {
            when {
                permissionState.allPermissionsGranted -> { viewModel.handleAction(AddressFinderViewModel.Action.DidTapFindMyLocationButton) }
                permissionState.shouldShowRationale -> { viewModel.handleAction(ShoudShowSettingAlert) }
                else -> permissionState.launchMultiplePermissionRequest()
            }
        }
        AddressList(uiState.addresses, onAddressItemClick)
    }
}


@Composable
fun AddressList(
    addresses: List<String>,
    onAddressItemClick: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(addresses) { address ->
            AddressBulletItem(
                address = address,
                onClick = { onAddressItemClick(address) }
            )
        }
    }
}

@Composable
fun AddressBulletItem(
    address: String,
    onClick: () -> Unit
) {
    var isSelected by rememberSaveable(address) { mutableStateOf(false) }
    val selectColor = if (isSelected) CS.PrimaryOrange.O40 else CS.Gray.G90
    val selectFont = if (isSelected) Typography.body1Bold else Typography.body1Regular

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isSelected = !isSelected
                onClick()
            }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "•", style = selectFont, color = selectColor, modifier = Modifier
            .padding(end = 8.dp)
        )
        Text(text = address, style = selectFont, color = selectColor, modifier = Modifier
            .weight(1f)
        )

        /* 선택된 경우에만 체크 아이콘 표시 */
        if (isSelected) {
            CheckCircleFill(24.dp, 24.dp)
        }
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