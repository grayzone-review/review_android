package preset_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.Review
import com.example.presentation.designsystem.typography.Typography
import com.team.common.feature_api.utility.Utility
import preset_ui.icons.ChatLine
import preset_ui.icons.LikeHeartFill
import preset_ui.icons.LikeHeartLine
import preset_ui.icons.StarFilled
import preset_ui.icons.StarHalf
import preset_ui.icons.StarOutline

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReviewCard(review: Review, isFullMode: Boolean, modifier: Modifier, onReviewCardClick: () -> Unit, onLikeReviewButtonClock: () -> Unit, onCommentButtonClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .combinedClickable(interactionSource = interactionSource, indication = null, onClick = onReviewCardClick)
    ) {
        WriterProfile(review, Modifier.padding(top = 20.dp))
        RatingSummary(review, Modifier.padding(vertical = 16.dp))
        if (isFullMode) RatingBox(review, Modifier)
        ReviewTextContent(review, isFullMode = isFullMode, Modifier.padding(top = 20.dp), onLikeReviewButtonClick = onLikeReviewButtonClock, onCommentButtonClick)
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

@Composable
fun ReviewTextContent(review: Review, isFullMode: Boolean, modifier: Modifier = Modifier, onLikeReviewButtonClick: () -> Unit, onCommentButtonClick: () -> Unit ) {
    val textContentItems: List<Pair<String, String>> = listOf(
        "장점"     to review.advantagePoint,
        "단점"     to review.disadvantagePoint,
        "바라는점"  to review.managementFeedback
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        ReviewTextContentTitle(title = review.title)
        Spacer(Modifier.height(16.dp))
        for ((index, sectionItem) in textContentItems.withIndex()) {
            if (!isFullMode && index > 0) {
                Text("더보기", color = CS.Gray.G50, style = Typography.body1Regular, modifier = Modifier.padding(start = 10.dp))
                break
            }
            ReviewSectionRow(sectionItem)
            Spacer(Modifier.height(if (index == 2) 16.dp else 32.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InteractionButton(count = review.likeCount, modifier = Modifier, onClick = onLikeReviewButtonClick) {
                if (review.liked) {
                    LikeHeartFill(width = 24.dp, height = 24.dp, modifier = Modifier.padding(all = 10.dp))
                } else {
                    LikeHeartLine(width = 24.dp, height = 24.dp, modifier = Modifier.padding(all = 10.dp))
                }
            }
            InteractionButton(count = review.commentCount, modifier = Modifier, onClick = onCommentButtonClick) {
                ChatLine(width = 24.dp, height = 24.dp, modifier = Modifier.padding(all = 10.dp))
            }
        }
    }
}

@Composable
fun ReviewTextContentTitle(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = title, color = CS.Gray.G90, style = Typography.h3)
    }
}

@Composable
fun ReviewSectionRow(item: Pair<String, String>) {
    val category = item.first
    val content = item.second
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start
    ) {
        TagChip(tag = category)
        Text(text = content, color = CS.Gray.G80, style = Typography.body1Regular, modifier = Modifier.alignByBaseline())
    }
}

@Composable
fun TagChip(tag: String) {
    val (backgroundColor, textColor) = when (tag) {
        "장점"     -> CS.SemanticBlue.B10 to CS.SemanticBlue.B50
        "단점"     -> CS.SemanticRed.R10  to CS.SemanticRed.R50
        "바라는점" -> CS.Gray.G10          to CS.Gray.G50
        else       -> CS.Gray.G10        to CS.Gray.G80
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = tag, color = textColor, style = Typography.captionBold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun InteractionButton(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = CS.Gray.Black),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        icon()
        Text(
            text  = count.toString(),
            style = Typography.body1Bold
        )
    }
}