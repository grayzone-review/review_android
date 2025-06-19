package create_review_dialog.sheet_contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import create_review_dialog.BottomSheetState
import create_review_dialog.CreateReviewUIState
import create_review_dialog.type.InputContentType
import create_review_dialog.type.InputField
import create_review_dialog.type.minMax
import create_review_dialog.type.title
import preset_ui.icons.ReviewCheckLine

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
                val text = when (inputField) {
                    InputField.JobRole -> uiState.jobRole
                    InputField.Advantage -> uiState.advantagePoint
                    InputField.Disadvantage -> uiState.disadvantagePoint
                    InputField.ManagementFeedback -> uiState.managementFeedBack
                    else -> ""
                }

                TextContent(
                    text = text,
                    onTextChange = { },
                    onSave = { },
                    minChars = inputField.minMax.first,
                    maxChars = inputField.minMax.last
                )
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
fun TextContent(
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    minChars: Int,
    maxChars: Int
) {
    val savable = text.length in minChars..maxChars

    Column(

    ) {
        // ── 입력 영역 ────────────────────────────
        BasicTextField(
            value = text,
            onValueChange = { if (it.length <= maxChars) onTextChange(it) },
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            decorationBox = { inner ->
                if (text.isEmpty()) {
                    Text(
                        text = "담당하신 역할을 입력해주세요 ex) 서빙",
                        style = Typography.body1Regular,
                        color = CS.Gray.G50
                    )
                } else inner()
            }
        )

        // ── 하단 정보 + 저장 ─────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${text.length}/${minChars}자 - ${maxChars}자",
                style = Typography.captionRegular,
                color = CS.Gray.G70
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "저장",
                style = Typography.body1Bold,
                color = if (savable) CS.PrimaryOrange.O40 else CS.PrimaryOrange.O20,
                modifier = Modifier.clickable(enabled = savable) { onSave() }
            )
        }
    }
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
