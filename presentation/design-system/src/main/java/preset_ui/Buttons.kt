package preset_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography

@Composable
fun IconTextToggleButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconEnabled: Painter,
    iconDisabled: Painter,
    iconSize: Dp,
    spaceBetween: Dp = 8.dp
) {
    val backgroundColor = if (enabled) CS.PrimaryOrange.O40  else CS.Gray.White
    val contentColor    = if (enabled) CS.Gray.White         else CS.PrimaryOrange.O40
    val icon            = if (enabled) iconEnabled           else iconDisabled

    Button(
        onClick = onClick,
        enabled = true,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, CS.PrimaryOrange.O40),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor   = contentColor
        )
    ) {
        Icon(
            painter              = icon,
            contentDescription   = null,
            modifier             = Modifier.size(iconSize),
            tint                 = Color.Unspecified
        )
        Spacer(Modifier.width(spaceBetween))
        Text(
            text  = text,
            style = Typography.body1Bold
        )
    }
}

@Composable
fun PrimaryIconTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter,
    iconSize: Dp,
    spaceBetween: Dp = 8.dp,
    cornerRadius: Dp
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40, // 오렌지 배경
            contentColor   = CS.Gray.White         // 흰색 텍스트·아이콘
        )
    ) {
        Icon(
            painter           = icon,
            contentDescription= null,
            modifier          = Modifier.size(iconSize),
            tint              = Color.Unspecified
        )
        Spacer(Modifier.width(spaceBetween))
        Text(
            text  = text,
            style = Typography.body1Bold
        )
    }
}