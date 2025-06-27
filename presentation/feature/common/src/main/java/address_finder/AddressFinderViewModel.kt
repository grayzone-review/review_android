package address_finder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddressFinderUIState(
    val addresses: List<String> = emptyList(),
    val shouldShowSettingAlert: Boolean = false
)

class AddressFinderViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        GetAddresses,
        DidTapFindMyLocationButton,
        ShoudShowSettingAlert,
        DismissSettingAlert
    }

    private val _uiState = MutableStateFlow(AddressFinderUIState())
    val uiState: StateFlow<AddressFinderUIState> = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.GetAddresses -> {
                val query = value as? String ?: return
                _uiState.update { it.copy(addresses = allAddresses) }
            }
            Action.DidTapFindMyLocationButton -> {
                
            }
            Action.ShoudShowSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = true) }
            }
            Action.DismissSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = false) }
            }
        }
    }

    private val allAddresses = listOf(
        "제주특별자치도 제주시 표선면",
        "서울특별시 종로구 청운동",
        "서울특별시 종로구 신교동",
        "서울특별시 종로구 궁정동",
        "서울특별시 종로구 효자동",
        "서울특별시 종로구 창성동",
        "서울특별시 종로구 통의동",
        "서울특별시 종로구 보광동",
        "서울특별시 종로구 사직동",
        "부산광역시 해운대구 중동",
        "부산광역시 해운대구 우동",
        "부산광역시 해운대구 재송동",
        "대구광역시 달서구 두류동",
        "대구광역시 달서구 상인동",
        "대전광역시 서구 둔산동",
        "광주광역시 서구 화정동",
        "경기도 성남시 분당구 정자동",
        "경기도 고양시 일산서구 주엽동",
        "경기도 수원시 영통구 영통동",
        "강원특별자치도 춘천시 석사동"
    )

}