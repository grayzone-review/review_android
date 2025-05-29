package preset_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Color
import colors.CS

@Composable
fun CSSpacerHorizontal(
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

@Composable
fun CSSpacerVertical(
    modifier: Modifier = Modifier,
    width: Dp,
    color: Color = CS.Gray.G10,
) {
    Spacer(
        modifier = modifier
            .width(width)           // 고정 가로폭
            .fillMaxHeight()        // 세로로 가능한 한 꽉 채움
            .background(color = color, shape = RectangleShape)
    )
}