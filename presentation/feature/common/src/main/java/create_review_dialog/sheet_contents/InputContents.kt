package create_review_dialog.sheet_contents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.SearchedCompany
import com.example.presentation.designsystem.typography.Typography
import create_review_dialog.CreateReviewUIState
import create_review_dialog.type.InputContentType
import create_review_dialog.type.InputField
import create_review_dialog.type.minMax
import create_review_dialog.type.placeholder
import create_review_dialog.type.title
import preset_ui.icons.CheckCircleFill
import preset_ui.icons.CloseFillIcon
import preset_ui.icons.ReviewCheckLine
import preset_ui.icons.SearchLineIcon

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
    onCloseButtonClick: () -> Unit,
    onSaveButtonClick: (InputField, String) -> Unit,
    onChangeSearchCompaniesQuery: (String) -> Unit,
    onCompanyItemClick: (SearchedCompany) -> Unit,
    onClickClearButton: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(CS.Gray.White)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        if (inputField.inputType != InputContentType.Company) TitleBar(text = inputField.title)

        when (inputField.inputType) {
            InputContentType.Company -> {
                CompanyContent(
                    uiState = uiState,
                    onTextChange = { onChangeSearchCompaniesQuery(it) },
                    onCompanyItemClick = { onCompanyItemClick(it) },
                    onClearButtonClick = { onClickClearButton() }
                )
            }

            InputContentType.Text -> {
                // InputField 별로 TextField 와 바인딩 할 uiState 결정
                val fieldText = when (inputField) {
                    InputField.JobRole -> uiState.jobRole
                    InputField.Advantage -> uiState.advantagePoint
                    InputField.Disadvantage -> uiState.disadvantagePoint
                    InputField.ManagementFeedback -> uiState.managementFeedBack
                    else -> ""
                }
                TextContent(
                    initialText = fieldText,
                    placeholder = inputField.placeholder,
                    onSaveClick = { onSaveButtonClick(inputField, it) },
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
fun CompanyContent(
    uiState: CreateReviewUIState,
    onTextChange: (String) -> Unit,
    onCompanyItemClick: (SearchedCompany) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    SearchTextField(
        text = uiState.searchTextFieldValue,
        onTextChange = { onTextChange(it) },
        onClickClearButton = { onClearButtonClick() }
    )
    if (uiState.searchedCompanies.isEmpty()) {
        EmptyView()
    } else {
        SearchResultList(
            companies = uiState.searchedCompanies,
            onSelect = { onCompanyItemClick(it) }
        )
    }
}

@Composable
fun TextContent(
    initialText: String,
    placeholder: String,
    onSaveClick: (String) -> Unit,
    minChars: Int,
    maxChars: Int
) {
    var text by rememberSaveable { mutableStateOf(initialText) }
    val savable = text.length in minChars..maxChars && text != initialText

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { if (it.length <= maxChars) text = it },
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            decorationBox = { innerTextField ->
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = Typography.body1Regular,
                        color = CS.Gray.G50
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.clickable(enabled = savable) { onSaveClick(text) }
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

@Composable
private fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onClickClearButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) CS.Gray.G90 else CS.Gray.G20

    Row(
        modifier = Modifier
            .height(90.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = modifier
                .weight(1f)
                .height(52.dp)
                .clip(shape)
                .border(1.dp, color = borderColor, shape = shape)
                .onFocusChanged { state -> isFocused = state.isFocused },
            textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
            decorationBox = searchDecorationBox(
                text = text,
                onClickClearButton = onClickClearButton
            ),
            singleLine = true,
            maxLines = 1
        )
    }
}

@Composable
fun SearchResultList(
    companies: List<SearchedCompany>,
    onSelect: (SearchedCompany) -> Unit,
) {
   LazyColumn(
       modifier = Modifier
           .height(580.dp)
   ) {
       items(
           items = companies,
           key = { it.id }
       ) { company ->
           SearchResultItem(
               company = company,
               onClick = { onSelect(company) }
           )
       }
   }
}

@Composable
private fun SearchResultItem(
    company: SearchedCompany,
    onClick: () -> Unit
) {
    /* ─ 선택 여부를 내부에서만 기억 ─ */
    var selected by remember { mutableStateOf(false) }
    val backgroundColor = if (selected) CS.Gray.G20 else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable {
                selected = !selected
                onClick()
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = company.companyName, style = Typography.body1Bold, color = CS.Gray.G90)
            Spacer(Modifier.height(8.dp))
            Text(text = company.companyAddress, style = Typography.captionRegular, color = CS.Gray.G50)
        }

        if (selected) {
            CheckCircleFill(24.dp, 24.dp, tint = CS.PrimaryOrange.O40)
        }
    }
}

@Composable
private fun EmptyView() {
    val textRes = "검색 결과를 찾을 수 없습니다."
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(95.dp))
        SearchLineIcon(48.dp, 48.dp, tint = CS.Gray.G30)
        Spacer(Modifier.height(12.dp))
        Text(text = textRes, color = CS.Gray.G50, style = Typography.body1Regular)
    }
}

@Composable
fun searchDecorationBox(
    text: String,
    onClickClearButton: () -> Unit
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { inner ->
    val shape = RoundedCornerShape(8.dp)
    val isEmptyTextFieldValue = text.isEmpty()

    Box(
        modifier = Modifier
            .background(Color.White, shape)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchLineIcon(24.dp, 24.dp, tint = CS.Gray.G90)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                // 2. Placeholder
                if (isEmptyTextFieldValue) {
                    Text(text = "상호명으로 검색하기", style = Typography.body1Regular, color = CS.Gray.G50)
                }

                inner()
            }
            if (!isEmptyTextFieldValue) {
                IconButton(onClick = onClickClearButton) {
                    CloseFillIcon(width = 24.dp, height = 24.dp)
                }
            }
        }
    }
}