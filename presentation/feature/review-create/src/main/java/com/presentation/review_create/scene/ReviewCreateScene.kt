package com.presentation.review_create.scene

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCreateScene(
    viewModel: ReviewCreateViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 제목 입력
        OutlinedTextField(
            value = title,
            onValueChange = { 
                title = it
                viewModel.updateTitle(it)
            },
            label = { Text("제목") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // 내용 입력
        OutlinedTextField(
            value = content,
            onValueChange = { 
                content = it
                viewModel.updateContent(it)
            },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            minLines = 5
        )

        // 저장 버튼
        Button(
            onClick = { viewModel.saveReview() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("리뷰 저장")
        }
    }
}
