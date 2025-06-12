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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.AroundIcon
import preset_ui.icons.CloseFillIcon
import preset_ui.icons.InterestIcon
import preset_ui.icons.MytownIcon
import com.feature.comments.scene.contents.BeforeContentViewModel.Action.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BeforeContent(
    viewModel: BeforeContentViewModel = hiltViewModel(),
    onClickTag: (String) -> Unit
) {
    val recentQueries by viewModel.recentQueries.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(DidAppear)
    }
    
    Spacer(Modifier.height(20.dp))
    RecentQueryList(
        title = "최근 검색어",
        recentQueries = recentQueries,
        onClickTag = { onClickTag(it) },
        onClickDelete = { viewModel.handleAction(DidTapQueryTagDeleteButton, index = it) }
    )
    Spacer(Modifier.height(40.dp))
    FilterButtons(
        title = "모아보기",
        buttons = listOf(
            TagButtonData("내 근처 업체") { AroundIcon(isOn = true, 18.dp, 18.dp) },
            TagButtonData("우리동네 업체") { MytownIcon(isOn = true, 18.dp, 18.dp) },
            TagButtonData("관심동네 업체") { InterestIcon(isOn = true, 18.dp, 18.dp) }
        ),
        onClick = { button ->
            when (button.label) {
                "내 근처 업체" -> {
                    // TODO: 내 근처 업체 처리 로직
                }

                "우리동네 업체" -> {
                    // TODO: 우리동네 업체 처리 로직
                }

                "관심동네 업체" -> {
                    // TODO: 관심동네 업체 처리 로직
                }
            }
        }
    )
}

@Composable
private fun RecentQueryList(title: String, recentQueries: List<String>, onClickTag: (String) -> Unit, onClickDelete: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        Text(text = title, style = Typography.body1Bold, color = CS.Gray.G90, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(recentQueries) { index, query ->
                TagView(
                    text = query,
                    onClickTag = { onClickTag(query) },
                    onClickDelete = { onClickDelete(index) }
                )
            }
        }
    }
}

@Composable
private fun TagView(text: String, onClickTag: () -> Unit, onClickDelete: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = CS.Gray.G20, shape = shape)
            .background(color = CS.Gray.White, shape = shape)
            .padding(horizontal = 12.dp, vertical = 11.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickTag()
            }
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
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // ← Ripple 제거
                    ) {
                        onClickDelete()
                    },
                contentAlignment = Alignment.Center
            ) {
                CloseFillIcon(width = 18.dp, height = 18.dp)
            }
        }
    }
}

@Composable
fun FilterButtons(
    title: String,
    buttons: List<TagButtonData>,
    onClick: (TagButtonData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        Text(text = title, style = Typography.body1Bold, color = CS.Gray.G90)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(buttons.take(3)) { button ->
                TagButtonItem(button, onClick)
            }
        }
    }
}


data class TagButtonData(val label: String, val icon: @Composable () -> Unit)
@Composable
fun TagButtonItem(data: TagButtonData, onClick: (TagButtonData) -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .background(color = CS.Gray.G10, shape = shape)
            .clickable { onClick(data) }
            .padding(horizontal = 8.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        data.icon()
        Text(
            text = data.label,
            style = Typography.captionSemiBold,
            color = CS.Gray.G70
        )
    }
}