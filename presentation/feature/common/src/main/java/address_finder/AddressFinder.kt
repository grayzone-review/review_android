package address_finder

import gps_setting_checker.GpsSettingChecker
import address_finder.AddressFinderViewModel.Action.DismissSettingAlert
import address_finder.AddressFinderViewModel.Action.FindMyLocation
import address_finder.AddressFinderViewModel.Action.ShoudShowSettingAlert
import address_finder.AddressFinderViewModel.Action.UpdateAddressesFromChangingQuery
import address_finder.AddressFinderViewModel.Action.UpdateAddressesFromScrollAtEnd
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.data.location.UpLocationService
import com.domain.entity.LegalDistrictInfo
import com.domain.entity.Region
import com.example.presentation.designsystem.typography.Typography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.team.common.feature_api.extension.openAppSettings
import common_ui.UpAlertIconDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import preset_ui.icons.CheckCircleFill
import preset_ui.icons.MapPinTintable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddressFinder(
    query: String,
    viewModel: AddressFinderViewModel = hiltViewModel(),
    onFindMyLocationButtonClick: (Region) -> Unit,
    onAddressItemClick: (LegalDistrictInfo) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val permissionState = rememberMultiplePermissionsState(UpLocationService.locationPermissions.toList())
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    GpsSettingChecker()
    LaunchedEffect(query) { viewModel.handleAction(UpdateAddressesFromChangingQuery, query) }
    LaunchedEffect(Unit) {
        viewModel.event.collect { locationEvent ->
            when (locationEvent) {
                is LocationEvent.RegionFound -> { onFindMyLocationButtonClick(locationEvent.region) }
            }
        }
    }

    SettingDialog(
        isShow = uiState.shouldShowSettingAlert,
        onConfirm = { context.openAppSettings() },
        onCancel = { viewModel.handleAction(DismissSettingAlert) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FindMyLocationButton(
            onClick = {
                scope.launch {
                    when {
                        permissionState.allPermissionsGranted -> { viewModel.handleAction(FindMyLocation) }
                        permissionState.shouldShowRationale -> { viewModel.handleAction(ShoudShowSettingAlert) }
                        else -> {
                            permissionState.launchMultiplePermissionRequest()
                            snapshotFlow { permissionState.allPermissionsGranted }
                                .filter { it }
                                .first()
                            viewModel.handleAction(FindMyLocation)
                        }
                    }
                }
            }
        )
        AddressList(
            legalDistricts =  uiState.legalDistrics,
            onAddressItemClick = onAddressItemClick,
            onReachedBottom = { viewModel.handleAction(UpdateAddressesFromScrollAtEnd) }
        )
    }
}

@Composable
fun AddressList(
    legalDistricts: List<LegalDistrictInfo>,
    onAddressItemClick: (LegalDistrictInfo) -> Unit,
    onReachedBottom: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .map { layout ->
                val total = layout.totalItemsCount
                val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastVisible >= total - 1 && total > 0       // 바닥 여부
            }
            .distinctUntilChanged()                         // true 한 번만 방출
            .collect { isEnd ->
                if (isEnd) onReachedBottom()
            }
    }


    Spacer(modifier = Modifier.height(10.dp))
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(legalDistricts) { index, legalDistrictInfo ->
            AddressBulletItem(
                index = index,
                legalDistrict = legalDistrictInfo,
                onClick = { onAddressItemClick(legalDistrictInfo) }
            )
        }
    }
}

@Composable
fun AddressBulletItem(
    index: Int,
    legalDistrict: LegalDistrictInfo,
    onClick: () -> Unit
) {
    var isSelected by rememberSaveable(legalDistrict) { mutableStateOf(false) }
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
        Text(text = legalDistrict.name, style = selectFont, color = selectColor, modifier = Modifier
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

@Composable
private fun SettingDialog(
    isShow: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!isShow) return

    UpAlertIconDialog(
        icon = { MapPinTintable(28.dp, 28.dp, tint = CS.Gray.White) },
        title = "위치 권한 필요",
        message = """
            기능을 사용하려면 위치 권한이 필요합니다.
            설정 > 권한에서 위치를 허용해주세요.
        """.trimIndent(),
        confirmText = "설정으로 이동",
        cancelText = "취소",
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}