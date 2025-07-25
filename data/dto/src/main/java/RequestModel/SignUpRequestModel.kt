package RequestModel

data class SignUpRequestModel(
    val oauthToken: String,
    val oauthProvider: String = "kakao",
    val mainRegionId: Int,
    val interestedRegionIds: List<Int>,
    val nickname: String,
    val agreements: List<String>
)