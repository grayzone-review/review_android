package com.presentation.company_detail.Scene.sheet

import androidx.lifecycle.ViewModel
import com.domain.entity.Comment
import com.domain.entity.Reply
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CommentInputState(
    val text: String = "",
    val isSecret: Boolean = false,
    val isSendable: Boolean = false
)

@HiltViewModel
class CommentBottomSheetViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidUpdateCommentText,
        DidCloseBottomSheet,
        DidTapSecretButton,
        DidTapSendButton
    }

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _repliesMap = MutableStateFlow<Map<Long, List<Reply>>>(emptyMap())
    val repliesMap: StateFlow<Map<Long, List<Reply>>> = _repliesMap.asStateFlow()

    private val _commentInputState = MutableStateFlow(CommentInputState())
    val commentInputState: StateFlow<CommentInputState> = _commentInputState.asStateFlow()

    init {
        _comments.value = generateMockComments()
        _repliesMap.value = generateMockReplies(_comments.value)
    }

    fun handleAction(action: Action, text: String? = null) {
        when (action) {
            Action.DidUpdateCommentText -> {
                text?.let { newText ->
                    _commentInputState.update { it.copy(
                        text = newText,
                        isSendable = isValid(newText)
                    )}
                }
            }
            Action.DidCloseBottomSheet -> {
                _commentInputState
            }
            Action.DidTapSecretButton -> {
                _commentInputState.update { it.copy(isSecret = !it.isSecret) }
            }
            Action.DidTapSendButton -> {
                if (_commentInputState.value.isSendable) {
                    // TODO: 전송 요청 로직
                }
            }
        }
    }

    private fun isValid(text: String): Boolean {
        if (text.length !in 1..200) return false
        if (text.firstOrNull()?.isWhitespace() == true || text.firstOrNull() == '\n') return false
        if (text.contains("\n\n\n")) return false
        if (text.contains("     ")) return false
        return true
    }

    fun reset() {
        _commentInputState.value = CommentInputState()
//        _isLoading.value = false
    }

    private fun generateMockComments(): List<Comment> = List(10) { index ->
        val hasReplies = index % 3 != 0 // 3의 배수 index는 답글 없음
        Comment(
            id = index.toLong(),
            comment = "가 $index 나 $index 다 $index 라 $index 마 $index 바 $index 사 $index 아 $index 자 $index 차 $index 카 $index 타 $index 파 $index 하 $index\n" +
                    "가 $index 나 $index 다 $index 라 $index 마 $index 바 $index 사 $index 아 $index 자 $index 차 $index 카 $index 타 $index 파 $index 하 $index\n" +
                    "가 $index 나 $index 다 $index 라 $index 마 $index 바 $index 사 $index 아 $index 자 $index 차 $index 카 $index 타 $index 파 $index 하 $index",
            authorName = "작성자이름 $index",
            createdAt = "2024-01-01 10:0$index",
            replyCount = if (hasReplies) 10 else 0,
            secret = index % 2 == 0,
            visible = true
        )
    }

    private fun generateMockReplies(comments: List<Comment>): Map<Long, List<Reply>> =
        comments.associate { comment ->
            comment.id to if (comment.replyCount > 0) {
                List(comment.replyCount) { i ->
                    Reply(
                        id = comment.id * 100 + i,
                        comment = "답글 $i for 댓글 ${comment.id}",
                        authorName = "답글 작성자 $i",
                        createdAt = "2024-01-01 11:0$i",
                        secret = i % 2 == 1,
                        visible = true
                    )
                }
            } else {
                emptyList()
            }
        }
}