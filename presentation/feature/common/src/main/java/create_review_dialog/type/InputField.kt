package create_review_dialog.type

enum class InputContentType { Company, Text, Period }

val InputContentType.sheetPartially: Boolean
    get() = when (this) {
        InputContentType.Company -> true
        InputContentType.Text -> false
        InputContentType.Period -> true
    }

sealed class InputField(val inputType: InputContentType) {
    data object Company: InputField(inputType = InputContentType.Company)
    data object JobRole: InputField(InputContentType.Text)
    data object EmploymentPeriod: InputField(InputContentType.Period)
    data object Advantage: InputField(InputContentType.Text)
    data object Disadvantage: InputField(InputContentType.Text)
    data object ManagementFeedback: InputField(InputContentType.Text)
}

val InputField.title: String
    get() = when (this) {
        InputField.Company            -> "상호명"
        InputField.JobRole            -> "업무 내용"
        InputField.EmploymentPeriod   -> "근무 기간"
        InputField.Advantage          -> "기업의 장점"
        InputField.Disadvantage       -> "기업의 단점"
        InputField.ManagementFeedback -> "경영진에게 한마디"
    }

val InputField.placeholder: String
    get() = when (this) {
        InputField.Company -> "상호명을 입력해주세요"
        InputField.JobRole -> "담당하신 역할을 입력해주세요 ex) 서빙"
        InputField.EmploymentPeriod -> "근무 기간을 선택해주세요"
        InputField.Advantage -> { "만족스러운 점은 무엇인가요?" }
        InputField.Disadvantage -> { "아쉬운 점은 무엇인가요?" }
        InputField.ManagementFeedback -> { "개선 되었으면 하는 점이 있나요?" }
    }

val InputField.minMax: IntRange
    get() = when (this) {
        InputField.Company, InputField.EmploymentPeriod -> 0..0
        InputField.JobRole -> 2..10
        InputField.Advantage, InputField.Disadvantage, InputField.ManagementFeedback -> 30..1000
    }