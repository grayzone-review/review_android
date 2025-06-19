package create_review_dialog.sheet_contents

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar

enum class InputContentType {
    Company, Text, Period
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


@Composable
fun InputContainer(inputField: InputField) {
    Column(

    ) {
        DefaultTopAppBar(title = inputField.title)
        when (inputField.inputType) {
            InputContentType.Company -> {
                CompanyContent()
            }
            InputContentType.Text -> {
                TextContent()
            }
            InputContentType.Period -> {
                PeriodContent()
            }
        }
    }
}

@Composable
fun CompanyContent() {

}

@Composable
fun TextContent() {

}

@Composable
fun PeriodContent() {

}