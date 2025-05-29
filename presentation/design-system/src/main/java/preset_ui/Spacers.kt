package preset_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Color
import colors.CS

@Composable
fun CSSpacer(
    modifier: Modifier = Modifier,
    height: Dp,
    color: Color = CS.Gray.G10,
) {
    Spacer(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .background(color = color, shape = RectangleShape)
    )
}