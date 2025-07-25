package RequestModel

data class WriteCommentRequestModel(
    val comment: String,
    val secret: Boolean
)