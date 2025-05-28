package com.presentation.company_detail.Scene

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ReviewDetailScene(viewModel: ReviewDetailViewModel) {
    val result = viewModel.reviewDetailTestText

    Text(text = result)
}