package RequestModel

data class AuthLoginRequestModel(
    val oauthToken: String,
    val oauthProvider: String = "kakao"
)
