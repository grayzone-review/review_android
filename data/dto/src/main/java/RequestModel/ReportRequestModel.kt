package RequestModel

data class ReportRequestModel(
    val reporterName: String,
    val targetName: String,
    val reportType: String,
    val description: String
)
