package com.presentation.company_detail.Scene.sheet

import BottomSheetHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.*
import preset_ui.CSSpacerHorizontal
import androidx.hilt.navigation.compose.hiltViewModel
import com.domain.entity.Comment
import com.domain.entity.Reply
import preset_ui.icons.RockClose
import preset_ui.icons.RockOpen
import preset_ui.icons.SendDisable
import preset_ui.icons.Sendable
import java.time.LocalDateTime

@Composable
fun CommentBottomSheet(
    viewModel: ReviewDetailViewModel,
    commentViewModel: CommentBottomSheetViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val inputState by commentViewModel.commentInputState.collectAsState()

    LaunchedEffect(viewModel.showBottomSheet) {
        if (viewModel.showBottomSheet) {
            commentViewModel.handleAction(DidAppear)
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
                        CommentList(
                            viewModel = commentViewModel,
                            onAddReplyClick = { commentViewModel.handleAction(DidTapWriteReplyButton, commentID = it) },
                            onShowMoreRepliesClick = { commentViewModel.handleAction(DidTapShowMoreRepliesButton, commentID = it) }
                        )
                    }
                }
            }
            BottomSheetHelper.setInputBar {
                InputBar(
                    inputState = inputState,
                    onTextChange = { commentViewModel.handleAction(DidUpdateCommentText, it) },
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
fun CommentList(
    viewModel: CommentBottomSheetViewModel,
    onAddReplyClick: (commentId: Long) -> Unit,
    onShowMoreRepliesClick: (commentId: Long) -> Unit
) {
    val nestedScrollConnection = rememberNestedScrollInteropConnection()
    val comments by viewModel.comments.collectAsState()
    val repliesMap by viewModel.repliesMap.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(comments) { comment ->
            val replies = repliesMap[comment.id].orEmpty()
            CommentCard(
                comment = comment,
                replies = replies,
                onAddReplyClick = { onAddReplyClick(comment.id) },
                onShowMoreRepliesClick = { onShowMoreRepliesClick(comment.id) }
            )
        }
    }
}

@Composable
fun CommentCard(
    comment: Comment,
    replies: List<Reply>,
    onAddReplyClick: () -> Unit,
    onShowMoreRepliesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(CS.SemanticBlue.B50)
            .padding(vertical = 10.dp)
    ) {
        CommentContent(comment = comment)
        Spacer(modifier = Modifier.height(8.dp))
        AddReplyButton(onAddReplyClick = onAddReplyClick)
        if (comment.replyCount > 0 && replies.isEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            ShowMoreButton(comment = comment, onShowMoreRepliesClick = onShowMoreRepliesClick)
        } else {
            ReplyList(replies = replies)
        }
    }
}

@Composable
fun CommentContent(comment: Comment) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = comment.authorName, color = CS.Gray.G90, style = Typography.body2Bold)
        Text(text = comment.comment, color = CS.Gray.G90, style = Typography.body1Regular)
    }
}

@Composable
fun AddReplyButton(onAddReplyClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            modifier = Modifier.height(16.dp),
            onClick = onAddReplyClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = CS.Gray.G50
            ),
            elevation = null,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("답글달기", color = CS.Gray.G50, style = Typography.captionBold)
        }
    }
}

@Composable
fun ShowMoreButton(comment: Comment, onShowMoreRepliesClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .height(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 12.dp, height = 1.dp)
                .background(CS.Gray.G20)
        )
        Spacer(modifier = Modifier.width(4.dp))
        // 만약, 버튼을 탭하면 -> 버튼이 사라지면서 답글을 나타내는 Column을 표시해줌
        Button(
            onClick = onShowMoreRepliesClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = CS.Gray.G50
            ),
            elevation = null,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "답글 ${comment.replyCount}개 더보기",
                color = CS.Gray.G50,
                style = Typography.captionBold
            )
        }
    }
}

@Composable
fun ReplyList(replies: List<Reply>) {
    val sortedDescReplies = replies.sortedByDescending { it.createdAt }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        sortedDescReplies.forEach { reply ->
            ReplyCard(reply = reply)
        }
    }
}

@Composable
fun ReplyCard(reply: Reply) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 12.dp, height = 1.dp)
                    .background(CS.Gray.G20)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = reply.authorName, color = CS.Gray.G90, style = Typography.body2Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = reply.comment, color = CS.Gray.G90, style = Typography.body1Regular, modifier = Modifier.padding(horizontal = 20.dp))
    }
}

@Composable
fun SecretReplyCard() {

}

@Composable
fun SheetTitle(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(37.dp),
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
        decorationBox = commentDecorationBox(
            inputState = inputState,
            isFocused = isFocused,
            onLockClick = onLockClick,
            onSendClick = onSendClick
        ),
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

