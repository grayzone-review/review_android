package com.presentation.main.scene

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    // 리뷰 생성 버튼 클릭 이벤트
    fun onReviewCreateClick() {
        // TODO: 리뷰 생성 화면으로 이동
    }

    // 리뷰 검색 버튼 클릭 이벤트
    fun onReviewSearchClick() {
        // TODO: 리뷰 검색 화면으로 이동
    }
}