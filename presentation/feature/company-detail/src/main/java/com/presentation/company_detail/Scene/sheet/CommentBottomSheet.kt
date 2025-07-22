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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.domain.entity.Comment
import com.domain.entity.Reply
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidBeginTextEditing
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidClearFocusState
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapCancelReplyButton
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapOutSideOfTextField
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapSecretButton
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapSendButton
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapShowMoreRepliesButton
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidTapWriteReplyButton
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.DidUpdateCommentText
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.GetComments
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.GetCommentsMore
import com.presentation.company_detail.Scene.sheet.CommentBottomSheetViewModel.Action.SetReviewID
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import preset_ui.CSSpacerHorizontal
import preset_ui.icons.CloseLine
import preset_ui.icons.RockClose
import preset_ui.icons.RockOpen
import preset_ui.icons.SendDisable
import preset_ui.icons.Sendable

@Composable
fun CommentBottomSheet(
    reviewID: Int,
    isShow: Boolean,
    viewModel: CommentBottomSheetViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isShow) {
        if (isShow) {
            viewModel.handleAction(SetReviewID, reviewID)
            viewModel.handleAction(GetComments)
            BottomSheetHelper.setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.handleAction(DidTapOutSideOfTextField) }
                        .imePadding()
                ) {
                    // 콘텐츠 영역
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        SheetTitle(modifier = Modifier.padding(top = 24.dp))
                        CommentList(
                            comments = uiState.comments,
                            repliesMap = uiState.repliesMap,
                            onAddReplyClick = {
                                focusRequester.requestFocus()
                                viewModel.handleAction(DidTapWriteReplyButton, commentID = it)
                            },
                            onShowMoreRepliesClick = { viewModel.handleAction(DidTapShowMoreRepliesButton, commentID = it) },
                            onLoadMoreComment = { viewModel.handleAction(GetCommentsMore) }
                        )
                    }
                }
            }
            BottomSheetHelper.setInputBar {
                InputBar(
                    inputState = uiState,
                    onTextChange = { viewModel.handleAction(DidUpdateCommentText, text = it) },
                    onLockClick = { viewModel.handleAction(DidTapSecretButton) },
                    onSendClick = { viewModel.handleAction(DidTapSendButton) },
                    didBeginTextEditing = { viewModel.handleAction(DidBeginTextEditing) },
                    onCancelReplyClick = { viewModel.handleAction(DidTapCancelReplyButton) },
                    didClearFocusState = { viewModel.handleAction(DidClearFocusState) },
                    focusRequester = focusRequester
                )
            }

            BottomSheetHelper.show(context = context)
        } else {
            viewModel.reset()
            BottomSheetHelper.hide()
        }
    }
}

@Composable
fun CommentList(
    comments: List<Comment>,
    repliesMap: Map<Int, List<Reply>>,
    onAddReplyClick: (Int) -> Unit,
    onShowMoreRepliesClick: (Int) -> Unit,
    onLoadMoreComment: () -> Unit
) {
    val nestedScrollConnection = rememberNestedScrollInteropConnection()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            lastVisible >= total - 3 && total > 0
        }
            .distinctUntilChanged()
            .filter { it }
            .collectLatest { onLoadMoreComment() }
    }

    LazyColumn(
        state = listState,
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
            ReplyList(replies = replies, targetComment = comment)
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
fun ReplyList(replies: List<Reply>, targetComment: Comment) {
    val sortedDescReplies = replies.sortedByDescending { it.createdAt }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        sortedDescReplies.forEach { reply ->
            if (reply.secret)
                ReplyCard(reply = reply, targetComment = targetComment)
            else
                SecretCard()
        }
    }
}

@Composable
fun ReplyCard(reply: Reply, targetComment: Comment) {
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
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = CS.PrimaryOrange.O40)) {
                    append("@" + targetComment.authorName)
                }
                append(" ") // 공백
                withStyle(style = SpanStyle(color = CS.Gray.G90)) {
                    append(reply.comment)
                }
            },
            style = Typography.body1Regular,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun SecretCard() {
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
            Text(text = "비밀댓글입니다.", color = CS.Gray.G50, style = Typography.body2Bold)
            Spacer(modifier = Modifier.width(2.dp))
            RockClose(width = 14.dp, height = 14.dp)
        }
    }
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
    onSendClick: () -> Unit,
    didBeginTextEditing: () -> Unit,
    onCancelReplyClick: () -> Unit,
    didClearFocusState: () -> Unit,
    focusRequester: FocusRequester,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 82.dp, max = 200.dp) // ← 최소 82, 최대 200 (4줄 기준)
            .background(CS.Gray.White)
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
                onSendClick = onSendClick,
                didBeginTextEditing = didBeginTextEditing,
                onCancelReplyClick = onCancelReplyClick,
                didClearFocusState = didClearFocusState,
                focusRequester = focusRequester
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
    onSendClick: () -> Unit,
    didBeginTextEditing: () -> Unit,
    onCancelReplyClick: () -> Unit,
    didClearFocusState: () -> Unit,
    focusRequester: FocusRequester
) {
    val shape = RoundedCornerShape(8.dp)
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val shouldClearFocus = inputState.shouldClearFocus

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus(force = true)
            didClearFocusState()
        }
    }

    BasicTextField(
        value = inputState.text,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp, max = 56.dp * 2) // 4줄까지 확장
            .clip(shape)
            .border(1.dp, color = CS.Gray.G20, shape = shape)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                if (isFocused) {
                    BottomSheetHelper.expand()
                    didBeginTextEditing()
                }
            },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Text
        ),
        textStyle = Typography.body1Regular.copy(color = CS.Gray.G90),
        decorationBox = commentDecorationBox(
            inputState = inputState,
            isFocused = isFocused,
            onLockClick = onLockClick,
            onSendClick = onSendClick,
            onCancelReplyClick = onCancelReplyClick
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
    onSendClick: () -> Unit,
    onCancelReplyClick: () -> Unit
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { inner ->
    val isReplyMode = inputState.isReplying && (inputState.replyToComment != null)

    Column {
        if (isReplyMode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(CS.Gray.G10),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "@${inputState.replyToComment?.authorName}에게 답글 남기는 중",
                        style = Typography.body2Regular.copy(color = CS.Gray.G50)
                    )
                    IconButton(
                        onClick = onCancelReplyClick
                    ) {
                        CloseLine(width = 18.dp, height = 18.dp, tint = CS.Gray.G50)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (inputState.text.isEmpty() && !isFocused && !isReplyMode) {
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
}

