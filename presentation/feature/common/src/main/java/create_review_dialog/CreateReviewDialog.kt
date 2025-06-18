package create_review_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import create_review_dialog.contents.FirstContent
import create_review_dialog.contents.SecondContent
import create_review_dialog.contents.ThirdContent

@Composable
fun CreateReviewDialog(
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        content(onDismiss = onDismiss, viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun content(
    onDismiss: () -> Unit,
    viewModel: CreateReviewDialogViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(title = "리뷰작성")
        when (uiState.phase) {
            CreateReviewPhase.First -> { FirstContent() }
            CreateReviewPhase.Second -> { SecondContent() }
            CreateReviewPhase.Third -> { ThirdContent() }
        }

        ReviewDialogButtons(
            phase = uiState.phase, onNext = { }, onBack = { }, onSubmit = { }
        )
    }
}

@Composable
private fun ReviewDialogButtons(
    phase: CreateReviewPhase,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        when (phase) {
            CreateReviewPhase.First -> {
                Button(onClick = onNext) {
                    Text("다음")
                }
            }

            CreateReviewPhase.Second -> {
                OutlinedButton(onClick = onBack) {
                    Text("이전")
                }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onNext) {
                    Text("다음")
                }
            }

            CreateReviewPhase.Third -> {
                OutlinedButton(onClick = onBack) {
                    Text("이전")
                }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onSubmit) {
                    Text("작성 완료")
                }
            }
        }
    }
}

