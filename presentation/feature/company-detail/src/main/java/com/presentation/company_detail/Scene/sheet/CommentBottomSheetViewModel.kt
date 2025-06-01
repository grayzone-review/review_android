package com.presentation.company_detail.Scene.sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentBottomSheetViewModel @Inject constructor() : ViewModel() {
    private val _commentText = MutableStateFlow("")
    val commentText: StateFlow<String> = _commentText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateCommentText(text: String) {
        _commentText.value = text
    }

    fun submitComment() {
        if (_commentText.value.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Repository를 통한 댓글 제출 로직 구현
                _commentText.value = ""
            } catch (e: Exception) {
                // TODO: 에러 처리
            } finally {
                _isLoading.value = false
            }
        }
    }

    enum class Action {
        DidUpdateCommentText,
        DidSubmitComment,
        DidCloseBottomSheet
    }
}