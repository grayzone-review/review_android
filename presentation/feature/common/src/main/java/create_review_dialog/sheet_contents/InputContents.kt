package create_review_dialog.sheet_contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import create_review_dialog.CreateReviewUIState
import preset_ui.icons.ReviewCheckLine

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

enum class WorkPeriod(val label: String) {
    UNDER_1_YEAR("1년 미만"),
    MORE_1_YEAR("1년 이상"),
    MORE_2_YEARS("2년 이상"),
    MORE_3_YEARS("3년 이상"),
    MORE_4_YEARS("4년 이상"),
    MORE_5_YEARS("5년 이상")
}


@Composable
fun InputContainer(
    uiState: CreateReviewUIState,
    inputField: InputField,
    onSelectPeriodItem: (WorkPeriod) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(CS.Gray.White)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TitleBar(text = inputField.title)
        when (inputField.inputType) {
            InputContentType.Company -> {
                CompanyContent()
            }
            InputContentType.Text -> {
                TextContent()
            }
            InputContentType.Period -> {
                PeriodContent(
                    selected = uiState.employmentPeriod,
                    onSelectPeriodItem = { onSelectPeriodItem(it) },
                    onCloseButtonClick = { onCloseButtonClick() }
                )
            }
        }
    }
}

@Composable
fun TitleBar(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(37.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text, style = Typography.body1Bold, color = CS.Gray.G90)
    }
}

@Composable
fun CompanyContent() {

}

@Composable
fun TextContent() {

}

@Composable
fun PeriodContent(
    selected: WorkPeriod?,
    onSelectPeriodItem: (WorkPeriod) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    val periods = listOf(
        WorkPeriod.UNDER_1_YEAR,
        WorkPeriod.MORE_1_YEAR,
        WorkPeriod.MORE_2_YEARS,
        WorkPeriod.MORE_3_YEARS,
        WorkPeriod.MORE_4_YEARS,
        WorkPeriod.MORE_5_YEARS
    )

    Column(Modifier.fillMaxWidth()) {
        periods.forEach { period ->
            val isSelected = period == selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { onSelectPeriodItem(period) }
                    .background(if (isSelected) CS.Gray.G10 else CS.Gray.White)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = period.label,
                    style = if (isSelected) Typography.body1Bold else Typography.body1Regular,
                    color = if (isSelected) CS.PrimaryOrange.O40 else CS.Gray.G90
                )
                Spacer(Modifier.weight(1f))
                if (isSelected) {
                    ReviewCheckLine(20.dp, 20.dp, tint = CS.PrimaryOrange.O40)
                }
            }
        }

        Text(
            text = "닫기",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCloseButtonClick)
                .padding(vertical = 24.dp),
            style = Typography.body1Regular,
            color = CS.Gray.G70,
            textAlign = TextAlign.Center
        )
    }
}
