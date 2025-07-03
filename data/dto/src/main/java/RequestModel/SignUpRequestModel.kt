package RequestModel

data class SignUpRequestModel(
    val oauthToken: String,
    val oauthProvider: String = "kakao",
    val mainRegionId: Long,
    val interestedRegionIds: List<Long>,
    val nickname: String,
    val agreements: List<String>
)