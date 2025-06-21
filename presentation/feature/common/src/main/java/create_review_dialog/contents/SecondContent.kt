package create_review_dialog.contents

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import com.example.presentation.designsystem.typography.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.Ratings
import com.domain.entity.SearchedCompany
import com.team.common.feature_api.utility.Utility
import create_review_dialog.CreateReviewUIState
import preset_ui.icons.StarFilled
import preset_ui.icons.StarHalf
import preset_ui.icons.StarOutline

enum class RatingKey(val getter: (Ratings) -> Double) {
    COMPANY_CULTURE({ it.COMPANY_CULTURE }),
    MANAGEMENT( { it.MANAGEMENT } ),
    SALARY( { it.SALARY } ),
    WELFARE( { it.WELFARE } ),
    WORK_LIFE_BALANCE( { it.WORK_LIFE_BALANCE } )
}

val RatingKey.label: String
    get() = when (this) {
        RatingKey.COMPANY_CULTURE -> "기업 문화"
        RatingKey.MANAGEMENT -> "경영진"
        RatingKey.SALARY -> "급여"
        RatingKey.WELFARE -> "복지"
        RatingKey.WORK_LIFE_BALANCE -> "워라밸"
    }

fun Ratings.score(key: RatingKey): Double = key.getter(this)

fun Ratings.update(key: RatingKey, new: Double): Ratings = when (key) {
    RatingKey.COMPANY_CULTURE -> copy(COMPANY_CULTURE = new)
    RatingKey.MANAGEMENT -> copy(MANAGEMENT = new)
    RatingKey.SALARY -> copy(SALARY = new)
    RatingKey.WELFARE -> copy(WELFARE = new)
    RatingKey.WORK_LIFE_BALANCE -> copy(WORK_LIFE_BALANCE = new)
}

fun Ratings.isFullyRated(): Boolean {
    return COMPANY_CULTURE > 0.0 &&
            MANAGEMENT > 0.0 &&
            SALARY > 0.0 &&
            WELFARE > 0.0 &&
            WORK_LIFE_BALANCE > 0.0
}

@Composable
fun SecondContent(
    uiState: CreateReviewUIState,
    onCompanyNameClick: () -> Unit,
    onRatingsChanged: (Ratings) -> Unit
) {
    CompanyNameRow(
        companyName = uiState.company?.companyName,
        ratings = uiState.rating,
        onCompanyNameClick = onCompanyNameClick
    )
    Column(
        Modifier.padding(start = 20.dp)
    ) {
        RatingSection(
            ratings = uiState.rating,
            onRatingsChanged = { onRatingsChanged(it) }
        )
    }
}

@Composable
private fun CompanyNameRow(
    companyName: String?,
    ratings: Ratings,
    onCompanyNameClick: () -> Unit
) {
    val ratingKeys: Array<RatingKey> = RatingKey.values()
    val nonZeroScores: List<Double> = ratingKeys
        .map { ratings.score(it) }
        .filter { it > 0.0 }
    val totalScore: Double = if (nonZeroScores.isNotEmpty()) nonZeroScores.average() else 0.0
    val textColor = if (totalScore == 0.0) CS.Gray.G50 else CS.Gray.G90

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(111.dp)
            .background(CS.Gray.G10),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = companyName ?: "" , style = Typography.h3, color = CS.PrimaryOrange.O40, modifier = Modifier.clickable { onCompanyNameClick()  })
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            StarRow(totalScore, resizable = false, onSelect = { })
            Spacer(Modifier.width(8.dp))
            Text("%.1f".format(totalScore), style = Typography.h1, color = textColor)
        }
    }
}

@Composable
fun RatingSection(
    ratings: Ratings,
    onRatingsChanged: (Ratings) -> Unit
) {
    val ratingKeys: Array<RatingKey> = RatingKey.values()
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val longestText = "기업 문화"
    val maxLabelWidthDp = remember {
        val sizePx = measurer.measure(
            AnnotatedString(longestText),
            style = Typography.body1Regular
        ).size.width
        with(density) { sizePx.toDp() }
    }

    Column(Modifier.fillMaxWidth()) {
        ratingKeys.forEach { ratingKey ->
            Row (
                modifier = Modifier.padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(ratingKey.label, style = Typography.body1Regular, color = CS.Gray.G90, modifier = Modifier.width(maxLabelWidthDp))
                Spacer(Modifier.width(20.dp))
                StarRow(
                    score = ratings.score(ratingKey),
                    resizable = true,
                    onSelect = {
                        val newRatings = ratings.update(ratingKey, new = it)
                        Log.d("ratings", newRatings.toString())
                        onRatingsChanged(newRatings)
                    }
                )
            }
        }
    }
}


@Composable
fun StarRow(
    score: Double,
    resizable: Boolean,
    onSelect: (Double) -> Unit
) {
    val counts = Utility.calculateStarCounts(score)

    val starSize = if (!resizable) {
        36.dp
    } else {
        val target = if (score == 0.0) 36.dp else 24.dp
        val size by animateDpAsState(
            targetValue = target,
            animationSpec = tween(durationMillis = 250)
        )
        size
    }

    Row(Modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(counts.full) { idx ->
            StarFilled(starSize, starSize, modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onSelect(idx + 1.0) })
        }
        repeat(counts.half) { idx ->
            val pos = counts.full + idx
            StarHalf(starSize, starSize, modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onSelect(pos + 1.0) }
            )
        }
        repeat(counts.empty) { idx ->
            val pos = counts.full + counts.half + idx
            StarOutline(starSize, starSize, modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onSelect(pos + 1.0) }
            )
        }
    }
}

