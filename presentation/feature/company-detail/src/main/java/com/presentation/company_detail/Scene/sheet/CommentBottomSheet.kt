package com.presentation.company_detail.Scene.sheet

import BottomSheetHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel.Action.DidCloseBottomSheet
import preset_ui.CSSpacerHorizontal
import androidx.hilt.navigation.compose.hiltViewModel
import preset_ui.icons.RockOpen
import preset_ui.icons.SendAble

@Composable
fun CommentBottomSheet(
    viewModel: ReviewDetailViewModel,
    commentViewModel: CommentBottomSheetViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val commentText by commentViewModel.commentText.collectAsState()
    val isLoading by commentViewModel.isLoading.collectAsState()

    // showBottomSheet가 true일 때 show, 아니면 hide
    LaunchedEffect(viewModel.showBottomSheet) {
        if (viewModel.showBottomSheet) {
            BottomSheetHelper.setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // 콘텐츠 영역
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        SheetTitle(modifier = Modifier.padding(top = 24.dp))
                        // 기타 콘텐츠
                    }
                }
            }
            BottomSheetHelper.setInputBar {
                InputBar(
                    commentText = commentText,
                    onTextChange = { commentViewModel.updateCommentText(it) },
                    onSubmit = { commentViewModel.submitComment() }
                )
            }

            BottomSheetHelper.show(peekHeightDp = 500, context)
        } else {
            BottomSheetHelper.hide()
        }
    }
}

@Composable
fun SheetTitle(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "댓글",
            style = Typography.body1Bold,
            color = CS.Gray.G90
        )
    }
}

@Composable
fun InputBar(
    commentText: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .background(CS.Gray.White)
    ) {
        CSSpacerHorizontal(height = 1.dp, color = CS.Gray.G20)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            CommentInputBar(
                value = commentText,
                onTextChange = onTextChange,
                onSubmit = onSubmit
            )
        }
    }
}

@Composable
fun CommentInputBar(
    value: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(shape)
            .border(1.dp, color = CS.Gray.G20, shape = shape)
            .onFocusChanged { isFocused = it.isFocused }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        decorationBox = commentDecorationBox(value = value, isFocused = isFocused),
        singleLine = false,
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun commentDecorationBox(
    value: String,
    isFocused: Boolean
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { inner ->
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty() && isFocused.not()) {
                Text(
                    "000님에게 댓글 추가…",
                    color = CS.Gray.G40,
                    style = Typography.body1Regular
                )
            }
            inner()
        }

        Spacer(modifier = Modifier.width(8.dp))
        RockOpen(width = 24.dp, height = 24.dp, modifier = Modifier.padding(vertical = 10.dp))
        Spacer(modifier = Modifier.width(8.dp))
        SendAble(width = 24.dp, height = 28.dp)
    }
}

