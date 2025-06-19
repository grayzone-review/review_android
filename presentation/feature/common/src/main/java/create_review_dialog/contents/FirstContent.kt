package create_review_dialog.contents

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.example.presentation.designsystem.typography.Typography.body1Regular
import create_review_dialog.CreateReviewUIState

@Composable
fun FirstContent(
    uiState: CreateReviewUIState,
    onCompanyClick: () -> Unit,
    onPeriodClick: () -> Unit,
    updateJobRole: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextFields(
            uiState = uiState,
            updateJobRole = { updateJobRole(it) }
        )
    }
}

@Composable
fun TextFields(
    uiState: CreateReviewUIState,
    updateJobRole: (String) -> Unit,
) {
    Text("상호명", style = Typography.h3, color = CS.Gray.G90)
    SimpleField(
        value = uiState.company?.companyName ?: "",
        onValueChange = {},
        placeholder = "상호명을 입력해주세요"
    )

    /* ── ② 업무 내용 ─────────────────────── */
    Text("업무 내용", style = Typography.h3, color = CS.Gray.G90)
    SimpleField(
        value = uiState.jobRole,
        onValueChange = updateJobRole,
        placeholder = "담당하신 역할을 입력해주세요 (ex. 서빙)"
    )

    /* ── ③ 근무 기간 ─────────────────────── */
    Text("근무 기간", style = Typography.h3, color = CS.Gray.G90)
    SimpleField(
        value = uiState.employmentPeriod,
        onValueChange = {},
        placeholder = "근무 기간을 선택해주세요",
        readOnly   = true,
        trailing   = {  },
        modifier   = Modifier.clickable {  }
    )
}


@Composable
private fun SimpleField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusModifier = Modifier.onFocusChanged { isFocused = it.isFocused }
    val strokeColor by animateColorAsState(
        targetValue = if (isFocused) CS.Gray.G90 else CS.Gray.G20,
        label = "underlineColor"
    )
    val textStyle = Typography.body1Regular

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        textStyle = textStyle,
        modifier = modifier
            .then(focusModifier)
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .drawBehind {
                val y = size.height - 1.dp.toPx()
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end   = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            },
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxWidth()) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = CS.Gray.G50
                    )
                }
                innerTextField()
            }
        }
    )
}
