package create_review_dialog.contents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import create_review_dialog.CreateReviewUIState
import preset_ui.SimpleTextFieldButton
import preset_ui.icons.ArrowDownIcon

@Composable
fun FirstContent(
    uiState: CreateReviewUIState,
    onCompanyClick: () -> Unit,
    onJobRoleClick: () -> Unit,
    onPeriodClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TextFields(
            uiState = uiState,
            onCompanyClick = onCompanyClick,
            onJobRoleClick = onJobRoleClick,
            onPeriodClick = onPeriodClick
        )
    }
}

@Composable
private fun TextFields(
    uiState: CreateReviewUIState,
    onCompanyClick: () -> Unit,
    onJobRoleClick: () -> Unit,
    onPeriodClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("상호명", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.company?.companyName ?: "",
            placeholder = "상호명을 입력해주세요",
            selectableMark = true,
            onClick = { onCompanyClick() }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("업무 내용", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.jobRole,
            placeholder = "담당하신 역할을 입력해주세요 (ex. 서빙)",
            selectableMark = false,
            onClick = { onJobRoleClick() }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("근무 기간", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.employmentPeriod?.label ?: "",
            placeholder = "근무 기간을 선택해주세요",
            selectableMark = true,
            onClick = { onPeriodClick() }
        )
    }
}

