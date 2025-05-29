package preset_ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import colors.CS

@Composable
fun StarFilled(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.star_origin),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.SemanticYellow.Y40
    )
}

@Composable
fun StarHalf(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.star_half_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun StarOutline(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.star_origin),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G20
    )
}
