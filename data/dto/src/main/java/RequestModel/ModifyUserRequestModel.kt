package RequestModel

data class ModifyUserRequestModel(
    val mainRegionId: Int,
    val interestedRegionIds: List<Int>,
    val nickname: String
)
