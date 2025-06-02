package com.presentation.company_detail.Scene.sheet

import BottomSheetHelper
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.*
import preset_ui.CSSpacerHorizontal
import androidx.hilt.navigation.compose.hiltViewModel
import preset_ui.icons.RockClose
import preset_ui.icons.RockOpen
import preset_ui.icons.SendDisable
import preset_ui.icons.Sendable

@Composable
fun CommentBottomSheet(
    viewModel: ReviewDetailViewModel,
    commentViewModel: CommentBottomSheetViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val inputState by commentViewModel.commentInputState.collectAsState()

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
                    inputState = inputState,
                    onTextChange = { commentViewModel.handleAction(DidUpdateCommentText, it)},
                    onLockClick = { commentViewModel.handleAction(DidTapSecretButton) },
                    onSendClick = { commentViewModel.handleAction(DidTapSendButton) }
                )
            }

            BottomSheetHelper.show(peekHeightDp = 500, context)
        } else {
            commentViewModel.reset()
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
    inputState: CommentInputState,
    onTextChange: (String) -> Unit,
    onLockClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 82.dp, max = 200.dp) // ← 최소 82, 최대 200 (4줄 기준)
            .background(CS.Gray.White)
            .imePadding()
    ) {
        CSSpacerHorizontal(height = 1.dp, color = CS.Gray.G20)
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            CommentInputBar(
                inputState = inputState,
                onTextChange = onTextChange,
                onLockClick = onLockClick,
                onSendClick = onSendClick
            )
        }
    }
}

@Composable
fun CommentInputBar(
    inputState: CommentInputState,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onLockClick: () -> Unit,
    onSendClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = inputState.text,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp, max = 56.dp * 2) // 4줄까지 확장
            .clip(shape)
            .border(1.dp, color = CS.Gray.G20, shape = shape)
            .onFocusChanged { isFocused = it.isFocused }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Text
        ),
        textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
        decorationBox = commentDecorationBox(inputState = inputState, isFocused = isFocused, onLockClick = onLockClick, onSendClick = onSendClick),
        singleLine = false,
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun commentDecorationBox(
    inputState: CommentInputState,
    isFocused: Boolean,
    onLockClick: () -> Unit,
    onSendClick: () -> Unit
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { inner ->
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (inputState.text.isEmpty() && !isFocused) {
                Text(
                    text = "000님에게 댓글 추가…",
                    color = CS.Gray.G40,
                    style = Typography.body1Regular
                )
            }
            inner()
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .clickable { onLockClick() },
            contentAlignment = Alignment.Center
        ) {
            if (inputState.isSecret) {
                RockClose(width = 28.dp, height = 28.dp)
            } else {
                RockOpen(width = 28.dp, height = 28.dp)
            }

        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .clickable { onSendClick() },
            contentAlignment = Alignment.Center
        ) {
            if (inputState.isSendable) {
                Sendable(width = 28.dp, height = 28.dp)
            } else {
                SendDisable(width = 28.dp, height = 28.dp)
            }
        }
    }
}

