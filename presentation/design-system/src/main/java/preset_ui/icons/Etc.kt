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
fun ChatLine(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.chat_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun ChatFill(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.chat_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}


@Composable
fun RockClose(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.rock_close),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun RockOpen(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.rock_open),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun Sendable(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.send_able),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun SendDisable(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.send_disable),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun CloseLine(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.close_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun BackBarButtonIcon(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.arrow_left_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}