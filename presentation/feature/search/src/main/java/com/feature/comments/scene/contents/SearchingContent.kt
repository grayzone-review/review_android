package com.feature.comments.scene.contents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.RecentCompany
import com.example.presentation.designsystem.typography.Typography
import com.feature.comments.scene.SearchUIState
import preset_ui.icons.ClockIcon
import preset_ui.icons.CloseLine
import preset_ui.icons.InfoIcon

@Composable
fun SearchingContent(searchUIState: SearchUIState) {
    // 1. Mock 데이터 정의
    val recentCompanies = listOf(
        RecentCompany(
            id = 1,
            companyName = "스타벅스 석촌역점",
            companyAddress = "서울특별시 송파구 백제고분로 358 1층"
        ),
        RecentCompany(
            id = 2,
            companyName = "버거킹 강남역점",
            companyAddress = "서울특별시 강남구 테헤란로 101"
        ),
        RecentCompany(
            id = 3,
            companyName = "투썸플레이스 건대입구점",
            companyAddress = "서울특별시 광진구 아차산로 200"
        )
    )

    // 2. 리스트 적용
    RecentViewedCompanies(
        recentCompanies = recentCompanies,
        onRemoveClick = {  }
    )
}


@Composable
private fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(95.dp))
        InfoIcon(48.dp, 48.dp)
        Spacer(Modifier.height(12.dp))
        Text(text = "최근 검색한 기록이 없습니다.", color = CS.Gray.G50, style = Typography.body1Regular)
    }
}

@Composable
private fun RecentViewedCompanies(
    recentCompanies: List<RecentCompany>,
    onRemoveClick: (RecentCompany) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(recentCompanies) { recentCompany ->
            RecentCompanyItem(
                company = recentCompany,
                onRemoveClick = { onRemoveClick(recentCompany) }
            )
        }
    }
}

@Composable
fun RecentCompanyItem(
    company: RecentCompany,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // ← 나머지 공간 모두 사용
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClockIcon(width = 20.dp, height = 20.dp, tint = CS.Gray.G50)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = company.companyName,
                    style = Typography.body1Bold,
                    color = CS.Gray.G90
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = company.companyAddress,
                style = Typography.body2Regular,
                color = CS.Gray.G50
            )
        }

        CloseLine(
            width = 20.dp,
            height = 20.dp,
            tint = CS.Gray.G50,
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable { onRemoveClick() }
        )
    }
}
