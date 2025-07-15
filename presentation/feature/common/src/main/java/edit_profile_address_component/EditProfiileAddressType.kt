package edit_profile_address_component

enum class FieldState {
    ClientError, Normal, ServerSuccess
}

data class NickNameField(
    val value: String = "",
    val fieldState: FieldState = FieldState.Normal,
    val errorMessage: String = "※ 2~12자 이내로 입력가능하며, 한글, 영문, 숫자 사용이 가능합니다."
)