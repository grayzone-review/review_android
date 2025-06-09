package com.feature.comments.scene.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.CloseFillIcon

@Composable
fun BeforeContent() {
    Spacer(Modifier.height(20.dp))
    TagRowWithTitle(title = "최근 검색어", tags = listOf("스타벅스 석촌점", "브로우레시피 잠실새내점", "호식이 두마리치킨 사가정점", "교촌치킨 서울점"))
}

@Composable
fun TagRowWithTitle(title: String, tags: List<String>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp)
    ) {
        Text(text = title, style = Typography.body1Bold, color = CS.Gray.G90)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(tags) { tag ->
                TagView(text = tag)
            }
        }
    }
}

@Composable
fun TagView(text: String) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = CS.Gray.G20, shape = shape)
            .background(color = CS.Gray.White, shape = shape)
            .padding(horizontal = 12.dp, vertical = 11.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = CS.Gray.G70,
                style = Typography.captionSemiBold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(18.dp) // 아이콘 크기 지정
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // ← Ripple 제거
                    ) {
                        // TODO: 클릭 이벤트
                    },
                contentAlignment = Alignment.Center
            ) {
                CloseFillIcon(width = 18.dp, height = 18.dp)
            }
        }
    }
}
