package preset_ui.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import colors.CS

@Composable
fun LikeHeartFill(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.heart_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.SemanticRed.R50
    )
}

@Composable
fun LikeHeartLine(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.heart_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}