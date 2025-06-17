package com.presentation.review_create.scene

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewCreateViewModel @Inject constructor() : ViewModel() {
    // 리뷰 제목
    private var _title = ""
    val title: String get() = _title

    // 리뷰 내용
    private var _content = ""
    val content: String get() = _content

    // 제목 업데이트
    fun updateTitle(newTitle: String) {
        _title = newTitle
    }

    // 내용 업데이트
    fun updateContent(newContent: String) {
        _content = newContent
    }

    // 리뷰 저장
    fun saveReview() {
        // TODO: 리뷰 저장 로직 구현
    }
}

