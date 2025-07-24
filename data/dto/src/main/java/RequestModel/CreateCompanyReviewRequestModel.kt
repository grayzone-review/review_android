package RequestModel

data class CreateCompanyReviewRequestModel(
    val advantagePoint: String,
    val disadvantagePoint: String,
    val managementFeedback: String,
    val jobRole: String,
    val employmentPeriod: String,
    val ratings: RatingsRequestModel
)

data class RatingsRequestModel(
    val welfare: Double,
    val workLifeBalance: Double,
    val salary: Double,
    val companyCulture: Double,
    val management: Double
)
