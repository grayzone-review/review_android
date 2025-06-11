package com.feature.comments.scene.contents

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.CS
import com.domain.entity.SearchedCompanies
import com.domain.entity.SearchedCompany
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.AroundIcon
import preset_ui.icons.FollowAddOffIcon
import preset_ui.icons.FollowPersonOnIcon
import preset_ui.icons.InterestIcon
import preset_ui.icons.MytownIcon
import preset_ui.icons.StarFilled

@Composable
fun AfterContent(
    viewModel: AfterContentViewModel
) {
    SearchResultList()
}

@Composable
private fun SearchResultList() {
    val sampleCompanies = SearchedCompanies(
        companies = listOf(
            SearchedCompany(
                id = 1,
                companyName = "스타벅스 석촌역점",
                companyAddress = "서울특별시 송파구 백제고분로 358 1층",
                totalRating = 4.0,
                reviewTitle = "복지가 좋고 경력 쌓기에 좋은 회사",
                distance = 0.8,
                following = false
            ),
            SearchedCompany(
                id = 2,
                companyName = "투썸플레이스 잠실점",
                companyAddress = "서울특별시 송파구 올림픽로 240",
                totalRating = 4.2,
                reviewTitle = "분위기 좋고 직원들이 친절함",
                distance = 1.2,
                following = true
            ),
            SearchedCompany(
                id = 3,
                companyName = "이디야커피 석촌호수점",
                companyAddress = "서울특별시 송파구 석촌호수로 135",
                totalRating = 3.8,
                reviewTitle = null,
                distance = 0.5,
                following = false
            )
        ),
        totalCount = 3,
        hasNext = false,
        currentPage = 1
    )

    var selected by remember { mutableStateOf("우리 동네 업체") }

    CompanyFilterChips(selected = selected, onClick = { selected = it })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        ResultCountText(searchedCompanies = sampleCompanies)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = sampleCompanies.companies,
            ) { company ->
                SearchedResultItem(
                    company = company,
                    onClick = { },
                    onFollowClick = { }
                )
            }
        }
    }
}


@Composable
private fun ResultCountText(searchedCompanies: SearchedCompanies) {
    Text(
        text = buildAnnotatedString {
            append("검색 결과 ")
            withStyle(style = SpanStyle(color = CS.Gray.G50)) {
                append(searchedCompanies.totalCount.toString())
            }
        },
        style = Typography.h3,
        color = CS.Gray.G90,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
private fun SearchedResultItem(
    company: SearchedCompany,
    onClick: () -> Unit = {},
    onFollowClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CS.Gray.G20, shape)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CS.Gray.White),
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = company.companyName, style = Typography.body1Bold, color = CS.Gray.Black)
                    Text(text = company.companyAddress, style = Typography.captionRegular, color = CS.Gray.G50)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StarFilled(16.dp, 16.dp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = company.totalRating.toString(), style = Typography.captionBold, color = CS.Gray.G90)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = company.companyAddress.take(n = 2), style = Typography.captionRegular, color = CS.Gray.G50)
                        Text(text = " · ", style = Typography.captionRegular, color = CS.Gray.G50)
                        Text(text = "${formatDistance(company.distance)}km", style = Typography.captionRegular, color = CS.Gray.G50)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onFollowClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (company.following) FollowPersonOnIcon(32.dp, 32.dp) else FollowAddOffIcon(32.dp, 32.dp)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (company.reviewTitle != null) {
                    Box(
                        modifier = Modifier
                            .background(color = CS.Gray.G10, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "한줄평", style = Typography.captionBold, color = CS.Gray.G50)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = company.reviewTitle ?: " ", style = Typography.captionRegular, color = CS.Gray.G70, modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun CompanyFilterChips(
    selected: String,
    onClick: (String) -> Unit
) {
    val items = listOf("우리 동네 업체", "내 근처 업체", "관심 동네 업체")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CS.Gray.G20)
            .background(CS.Gray.G10, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Row {
                    Text(text = "모아보기", style = Typography.body2Bold, color = CS.Gray.G90)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            items(items) { label ->
                val isSelected = label == selected
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (isSelected) CS.PrimaryOrange.O40 else CS.Gray.White,
                    border = if (isSelected) null else BorderStroke(1.dp, CS.Gray.G20),
                    modifier = Modifier
                        .height(40.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onClick(label) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        when (label) {
                            "우리 동네 업체" -> MytownIcon(isOn = !isSelected, 18.dp, 18.dp)
                            "내 근처 업체" -> AroundIcon(isOn = !isSelected, 18.dp, 18.dp)
                            "관심 동네 업체" -> InterestIcon(isOn = !isSelected, 18.dp, 18.dp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = label, style = Typography.captionSemiBold, color = if (isSelected) CS.Gray.White else CS.Gray.G70)
                    }
                }
            }
        }
    }
}


private fun formatDistance(distance: Double): Double {
    return String.format("%.1f", distance).toDouble()
}