package preset_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.Ratings
import com.domain.entity.Review
import com.example.presentation.designsystem.typography.Typography
import com.team.common.feature_api.utility.Utility
import kotlin.math.floor

@Composable
fun ReviewCard(review: Review) {
    Column(

    ) {
        WriterProfile(review, Modifier.padding(top = 20.dp))
        RatingSummary(review, Modifier.padding(vertical = 16.dp))
        RatingBox(review, Modifier)
        RatingSummary(review, Modifier.padding(top = 16.dp))
    }
}

@Composable
fun WriterProfile(review: Review, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = review.nickName, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.jobRole, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.employmentPeriod, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.createdAt, color = CS.Gray.G50, style = Typography.captionRegular)
    }
}

@Composable
fun RatingSummary(review: Review, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(23.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val score: Double = review.totalRating
        val starCounts = Utility.calculateStarCounts(score)
        val fullStars = starCounts.full
        val halfStars = starCounts.half
        val emptyStars = starCounts.empty

        Text(text = "${review.totalRating}", color = CS.Gray.G90, style = Typography.h3)
        repeat(fullStars) {
            StarFilled(width = 20.dp, height = 20.dp)
        }
        repeat(halfStars) {
            StarHalf(width = 20.dp, height = 20.dp)
        }
        repeat(emptyStars) {
            StarOutline(width = 20.dp, height = 20.dp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingBox(review: Review, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(8.dp)
    val ratings = review.ratings

    val ratingItems: List<Pair<String, Double>> = listOf(
        "조직문화"  to ratings.COMPANY_CULTURE,
        "경영진"   to ratings.MANAGEMENT,
        "급여"    to ratings.SALARY,
        "복지"    to ratings.WELFARE,
        "워라밸"   to ratings.WORK_LIFE_BALANCE
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp) // 박스 외부의 패딩
            .clip(shape)
            .background(CS.Gray.G10)
            .border(1.dp, CS.Gray.G20, shape)
    ) {
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp), // 내부의 컨텐츠 패딩
            horizontalArrangement = Arrangement.spacedBy(59.dp),
            verticalArrangement   = Arrangement.spacedBy(20.dp)
        ) {
            ratingItems.forEachIndexed { index, item ->
                val isFullLine = (index + 1) % 5 == 0      // 5, 10, 15… 번째면 한 줄 전부

                RatingBoxCell(
                    item = item,
                    modifier = Modifier.weight(1f)   // 두 칸 중 하나
                )

                // ── 마지막 아이템이면서 전체 개수가 홀수(=마지막 줄이 1개) ──
                val isLast = index == ratingItems.lastIndex
                if (isLast && ratingItems.size % 2 == 1) {
                    Spacer(modifier = Modifier.weight(1f))   // 남은 반 칸을 채워 줌
                }
            }
        }
    }
}

@Composable
fun RatingBoxCell(item: Pair<String, Double>, modifier: Modifier) {
    val category = item.first
    val score = item.second
    val starCounts = Utility.calculateStarCounts(score)

    Row(
        modifier = modifier
            .width(118.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category, color = CS.Gray.G50, style = Typography.captionRegular)
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(starCounts.full) {
                StarFilled(width = 12.dp, height = 12.dp)
            }
            repeat(starCounts.half) {
                StarHalf(width = 12.dp, height = 12.dp)
            }
            repeat(starCounts.empty) {
                StarOutline(width = 12.dp, height = 12.dp)
            }
        }
    }
}