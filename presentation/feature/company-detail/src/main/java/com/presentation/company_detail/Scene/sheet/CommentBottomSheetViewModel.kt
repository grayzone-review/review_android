package com.presentation.company_detail.Scene.sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Comment
import com.domain.entity.Reply
import com.domain.usecase.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentInputState(
    val reviewID: Int = 0,
    val comments: List<Comment> = emptyList(),
    val repliesMap: Map<Int, List<Reply>> = emptyMap(),
    val text: String = "",
    val isSecret: Boolean = false,
    val isSendable: Boolean = false,
    val replyToComment: Comment? = null,
    val isReplying: Boolean = false,
    val shouldClearFocus: Boolean = false,

    val currentPage: Int = 0,
    val hasNext: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class CommentBottomSheetViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        SetReviewID,
        GetComments,
        GetCommentsMore,
        DidUpdateCommentText,
        DidCloseBottomSheet,
        DidTapSecretButton,
        DidTapSendButton,
        DidTapWriteReplyButton,
        DidTapShowMoreRepliesButton,
        DidBeginTextEditing,
        DidTapCancelReplyButton,
        DidTapOutSideOfTextField,
        DidClearFocusState
    }

    private val _uiState = MutableStateFlow(CommentInputState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null, text: String? = null, commentID: Int? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.SetReviewID -> {
                val reviewID = value as? Int ?: return
                _uiState.update { it.copy(reviewID = reviewID) }
            }
            Action.GetComments -> {
                if (currentState.isLoading) return
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = reviewUseCase.reviewComments(reviewID = currentState.reviewID, page = 0)
                    _uiState.update {
                        it.copy(
                            comments = result.comments,
                            isLoading = false,
                            currentPage = currentState.currentPage + 1,
                            hasNext = result.hasNext
                        )
                    }
                }
            }
            Action.GetCommentsMore -> {
                if (currentState.isLoading || !currentState.hasNext) return
                val nextPage = currentState.currentPage
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = reviewUseCase.reviewComments(reviewID = currentState.reviewID, page = nextPage)
                    _uiState.update {
                        it.copy(
                            comments = result.comments,
                            isLoading = false,
                            currentPage = currentState.currentPage + 1,
                            hasNext = result.hasNext
                        )
                    }
                }
            }
            Action.DidBeginTextEditing -> {
                _uiState.update { it.copy(isSendable = isValid(it.text)) }
            }
            Action.DidUpdateCommentText -> {
                text?.let { newText ->
                    _uiState.update { it.copy(
                        text = newText,
                        isSendable = isValid(newText)
                    )}
                }
            }
            Action.DidCloseBottomSheet -> {
                // TODO: 초기화
            }
            Action.DidTapSecretButton -> {
                _uiState.update { it.copy(isSecret = !it.isSecret) }
            }
            Action.DidTapSendButton -> {
                if (_uiState.value.isSendable) {
                    // TODO: 전송 요청 로직
                }
            }
            Action.DidTapWriteReplyButton -> {
//                commentID?.let { targetID ->
//                    val targetComment = _comments.value.first { it.id == targetID }
//                    _uiState.update {
//                        it.copy(
//                            isReplying = true,
//                            replyToComment = targetComment,
//                            isSendable = isValid(text = it.text),
//                            isSecret = targetComment.secret
//                        )
//                    }
//                }
            }
            Action.DidTapShowMoreRepliesButton -> {
//                commentID?.let {
//                    val commentId: Int = it
//                    val currentMap = _repliesMap.value.toMutableMap()
//                    currentMap[commentId] = generateMockReplies(commentID = commentId)
//                    _repliesMap.value = currentMap.toMap()
//                }
            }

            Action.DidTapCancelReplyButton -> {
                _uiState.update {
                    it.copy(
                        isReplying = true,
                        replyToComment = null,
                        isSendable = isValid(text = it.text)
                    )
                }
            }
            Action.DidTapOutSideOfTextField -> {
                _uiState.update {
                    it.copy(
                        shouldClearFocus = true
                    )
                }
            }
            Action.DidClearFocusState -> {
                _uiState.update {
                    it.copy(
                        shouldClearFocus = false
                    )
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
        _uiState.value = CommentInputState()
//        _isLoading.value = false
    }
}