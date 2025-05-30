package preset_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import kotlin.math.floor

data class Review(
    val nickname: String,
    val jobDescription: String,
    val period: String,
    val createAt: String,
    val totalScore: Double,
    val title: String,
    val advantage: String,
    val disadbantage: String,
    val managementFeedback: String
)

@Composable
fun ReviewCard(review: Review) {
    Column(

    ) {
        WriterProfile(review, Modifier.padding(top = 20.dp))
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
        Text(text = review.nickname, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.jobDescription, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.period, color = CS.Gray.G50, style = Typography.captionRegular)
        CSSpacerVertical(modifier = Modifier, width = 1.dp, color = CS.Gray.G20)
        Text(text = review.createAt, color = CS.Gray.G50, style = Typography.captionRegular)
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
        val score: Double = review.totalScore
        val floored: Double = floor(score * 2.0) / 2.0
        val fullStars = floored.toInt()
        val halfStars = if (floored - fullStars >= 0.5) 1 else 0
        val emptyStars = 5 - fullStars - halfStars

        Text(text = "${review.totalScore}", color = CS.Gray.G90, style = Typography.h3)

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