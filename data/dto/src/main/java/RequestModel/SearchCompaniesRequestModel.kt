package RequestModel


data class SearchCompaniesRequestModel(
    val keyword: String,
    val latitude: Double,
    val longitude: Double,
    val size: Int,
    val page: Int
)