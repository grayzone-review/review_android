package address_finder

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.data.location.UpLocationService
import com.domain.entity.LegalDistrictInfo
import com.domain.entity.Region
import com.domain.usecase.KakaoMapUseCase
import com.domain.usecase.SearchDistrictUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationEvent {
    data class RegionFound(val region: Region) : LocationEvent
}

data class AddressFinderUIState(
    val query: String = "",
    val legalDistrics: List<LegalDistrictInfo> = emptyList(),
    val shouldShowSettingAlert: Boolean = false,
    val currentPage: Int = 0,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddressFinderViewModel @Inject constructor(
    private val kakaoMapUseCase: KakaoMapUseCase,
    private val searchDistrictUseCase: SearchDistrictUseCase
) : ViewModel() {
    enum class Action {
        UpdateAddressesFromChangingQuery,
        UpdateAddressesFromScrollAtEnd,
        FindMyLocation,
        ShoudShowSettingAlert,
        DismissSettingAlert
    }

    private val _uiState = MutableStateFlow(AddressFinderUIState())
    val uiState: StateFlow<AddressFinderUIState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<LocationEvent>()
    val event: SharedFlow<LocationEvent> = _event.asSharedFlow()

    @OptIn(UnstableApi::class)
    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateAddressesFromChangingQuery -> {
                val query = value as? String ?: return // 쿼리 없음 차단
                if (query.isNotBlank() && query == _uiState.value.query) return // 동일 쿼리 차단
                _uiState.update {
                    it.copy(
                        query = query,
                        legalDistrics = emptyList(),
                        currentPage = 0,
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    val result = searchDistrictUseCase.searchLegalDistrict(
                        keyword = query,
                        page = 0
                    )
                    _uiState.update {
                        it.copy(
                            legalDistrics = result.districts,
                            hasNext = result.hasNext,
                            isLoading = false
                        )
                    }
                }
            }
            Action.UpdateAddressesFromScrollAtEnd -> {
                val currentState = _uiState.value
                if (currentState.isLoading || !currentState.hasNext) return // 로딩, 담 페이지 없으면 차단
                val nextPage = currentState.currentPage + 1
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    val result = searchDistrictUseCase.searchLegalDistrict(
                        keyword = currentState.query,
                        page = nextPage
                    )
                    _uiState.update {
                        it.copy(
                            legalDistrics = it.legalDistrics + result.districts,
                            currentPage = nextPage,
                            hasNext = result.hasNext,
                            isLoading = false
                        )
                    }
                }
            }
            Action.FindMyLocation -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpLocationService.fetchCurrentLocation() ?: return@launch
                    val reverseGeocodingRegions = kakaoMapUseCase.reverseGeocoding(xLongitude = longitude, yLatitude = latitude)
                    val region = reverseGeocodingRegions.regions.first()
                    _event.emit(LocationEvent.RegionFound(region = region))
                }
            }
            Action.ShoudShowSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = true) }
            }
            Action.DismissSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = false) }
            }
        }
    }
}