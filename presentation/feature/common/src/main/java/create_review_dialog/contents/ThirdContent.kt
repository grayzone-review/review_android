package create_review_dialog.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import create_review_dialog.CreateReviewUIState
import preset_ui.SimpleTextFieldButton

@Composable
fun ThirdContent(
    uiState: CreateReviewUIState,
    onAdvantagePointClick: () -> Unit,
    onDisadvantagePointClick: () -> Unit,
    onManagementFeedBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TextFields(
            uiState = uiState,
            onAdvantagePointClick = onAdvantagePointClick,
            onDisadvantagePointClick = onDisadvantagePointClick,
            onManagementFeedBackClick = onManagementFeedBackClick
        )
    }
}

@Composable
private fun TextFields(
    uiState: CreateReviewUIState,
    onAdvantagePointClick: () -> Unit,
    onDisadvantagePointClick: () -> Unit,
    onManagementFeedBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("기업의 장점", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.advantagePoint,
            placeholder = "만족스러운 점은 무엇인가요?",
            selectableMark = false,
            onClick = { onAdvantagePointClick() }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("기업의 단점", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.disadvantagePoint,
            placeholder = "아쉬웠던 점은 무엇인가요?",
            selectableMark = false,
            onClick = { onDisadvantagePointClick() }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("경영진에게 한마디", style = Typography.h3, color = CS.Gray.G90)
        SimpleTextFieldButton(
            value = uiState.managementFeedBack,
            placeholder = "개선되었으면 하는 점이 있나요?",
            selectableMark = false,
            onClick = { onManagementFeedBackClick() }
        )
    }
}