package com.presentation.company_detail.Scene.sheet

import androidx.lifecycle.ViewModel
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

    private val _commentInputState = MutableStateFlow(CommentInputState())
    val commentInputState: StateFlow<CommentInputState> = _commentInputState.asStateFlow()

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
}