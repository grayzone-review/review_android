package com.presentation.company_detail.Scene.sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Comment
import com.domain.entity.Reply
import com.domain.usecase.ReviewUseCase
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CommentInputUIEvent {
    data class ShowAlert(val error: APIException? = null) : CommentInputUIEvent
}

data class CommentInputState(
    val reviewID: Int = 0,                                  // 회사 리뷰 ID
    val comments: List<Comment> = emptyList(),              // 전체 댓글 목록
    val repliesMap: Map<Int, List<Reply>> = emptyMap(),     // (댓글 ID) to 답글 목록

    /* 지금 작성 중인 텍스트 필드 값 + 각종 설정 */
    val text: String = "",                      // 텍필 텍스트
    val isSecret: Boolean = false,              // 비밀글 여부를 체크
    val isSendable: Boolean = false,            // 제출 가능 여부를 체크
    val replyToComment: Comment? = null,        // 답글을 작성 중 이라면, 답글을 남기고 있는 댓글
    val isReplying: Boolean = false,            // 답글을 작성 중인지 여부
    val shouldClearFocus: Boolean = false,      // 텍스트 필드가 포커스 소유 하는지 여부

    val currentPage: Int = 0,                   // 댓글의 현재 페이지
    val hasNext: Boolean = false,               // 댓글의 다음 페이지 존재 여부
    val isLoading: Boolean = false              // 댓글을 가져 오는 중인지 여부
)

@HiltViewModel
class CommentBottomSheetViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        GetCommentsMore,
        DidUpdateCommentText,
        DidTapSecretButton,
        DidTapSendButton,
        DidTapWriteReplyButton,
        DidTapShowRepliesButton,
        DidBeginTextEditing,
        DidTapCancelReplyButton,
        DidTapOutSideOfTextField,
        DidClearFocusState
    }

    private val _uiState = MutableStateFlow(CommentInputState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CommentInputUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                val reviewID = value as? Int ?: return

                if (currentState.isLoading) return
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(reviewID = reviewID, isLoading = true)
                    }
                    try {
                        val result = reviewUseCase.reviewComments(reviewID = currentState.reviewID, page = 0)
                        result?.let { bindingResult ->
                            val sortedComments = bindingResult.comments.sortedByDescending { it.createdAt }
                            _uiState.update {
                                it.copy(
                                    comments = sortedComments,
                                    isLoading = false,
                                    currentPage = currentState.currentPage + 1,
                                    hasNext = bindingResult.hasNext
                                )
                            }
                        }
                    } catch (error: APIException) {
                        _event.emit(CommentInputUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
            Action.GetCommentsMore -> {
                if (currentState.isLoading || !currentState.hasNext) return
                val nextPage = currentState.currentPage
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        val result = reviewUseCase.reviewComments(reviewID = currentState.reviewID, page = nextPage)
                        val sortedComments = result?.comments
                            ?.sortedByDescending { comment -> comment.createdAt }
                            .orEmpty()
                        _uiState.update {
                            it.copy(
                                comments = sortedComments,
                                isLoading = false,
                                currentPage = currentState.currentPage + 1,
                                hasNext = result?.hasNext ?: false
                            )
                        }
                    } catch (error: APIException) {
                        _event.emit(CommentInputUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
            Action.DidBeginTextEditing -> {
                _uiState.update { it.copy(isSendable = isValid(it.text)) }
            }
            Action.DidUpdateCommentText -> {
                val newText = value as? String ?: return
                _uiState.update {
                    it.copy(text = newText, isSendable = isValid(newText))
                }
            }
            Action.DidTapSecretButton -> {
                _uiState.update { it.copy(isSecret = !it.isSecret) }
            }
            Action.DidTapSendButton -> {
                if (!currentState.isSendable) return
                viewModelScope.launch {
                    /* 1. 댓글, 답글 여부에 따른 액션 */
                    try {
                        if (currentState.isReplying) {
                            val comment = currentState.replyToComment ?: return@launch
                            val commentId = comment.id
                            val result = reviewUseCase.writeReply(
                                commentID = currentState.replyToComment?.id ?: 0,
                                content = currentState.text,
                                isSecret = currentState.isSecret
                            )
                            result?.let { reply ->
                                val updatedList = (currentState.repliesMap[commentId].orEmpty() + reply).sortedByDescending { it.createdAt }
                                val updatedRepliesMap = currentState.repliesMap + (commentId to updatedList)
                                _uiState.update { it.copy(repliesMap = updatedRepliesMap) }
                            }
                        } else {
                            val result = reviewUseCase.writeComment(
                                reviewID = currentState.reviewID,
                                content = currentState.text,
                                isSecret = currentState.isSecret
                            )
                            result?.let { comment ->
                                val updatedComments = (listOf(comment) + currentState.comments)
                                    .sortedByDescending { it.createdAt }
                                _uiState.update { it.copy(comments = updatedComments) }
                            }
                        }
                        /* 2. 입력 상태 초기화 */
                        _uiState.update {
                            it.copy(
                                text = "",
                                isSecret = false,
                                isSendable = isValid(text = it.text),
                                replyToComment = null,
                                isReplying = false,
                                shouldClearFocus = false
                            )
                        }
                    } catch (error: APIException) {
                        _event.emit(CommentInputUIEvent.ShowAlert(error))
                    }
                }
            }
            Action.DidTapWriteReplyButton -> {
                val commentID = value as? Int ?: return
                val targetComment = currentState.comments.first { it.id == commentID }
                _uiState.update {
                    it.copy(
                        isReplying = true,
                        replyToComment = targetComment,
                        isSendable = isValid(text = it.text),
                        isSecret = targetComment.secret
                    )
                }
            }
            Action.DidTapShowRepliesButton -> {
                val commentID = value as? Int ?: return
                viewModelScope.launch {
                    try {
                        val result = reviewUseCase.commentReplies(commentID = commentID, page = 0)
                        result?.let { bindingResult ->
                            val sortedReplies = bindingResult.replies.sortedByDescending { it.createdAt }
                            _uiState.update {
                                it.copy(repliesMap = currentState.repliesMap + (commentID to sortedReplies))
                            }
                        }
                    } catch (error: APIException) {
                        _event.emit(CommentInputUIEvent.ShowAlert(error))
                    }
                }
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
                _uiState.update { it.copy(shouldClearFocus = true) }
            }
            Action.DidClearFocusState -> {
                _uiState.update { it.copy(shouldClearFocus = false) }
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
    }
}