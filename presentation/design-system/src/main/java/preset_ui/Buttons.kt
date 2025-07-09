package preset_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import preset_ui.icons.ArrowDownIcon
import preset_ui.icons.SearchLineIcon

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

@Composable
fun SimpleTextFieldButton(
    value: String,
    placeholder: String,
    selectableMark: Boolean,
    onClick: () -> Unit
) {
    val textStyle = Typography.body1Regular

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clickable(onClick = onClick)
            .drawBehind {
                val y = size.height - 1.dp.toPx()
                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (value.isEmpty()) placeholder else value,
            style = textStyle,
            color = if (value.isEmpty()) CS.Gray.G50 else CS.Gray.G90,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        if (selectableMark) {
            ArrowDownIcon(24.dp, 24.dp, tint = CS.Gray.G50, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}


@Composable
fun SimpleTextFieldOutlinedButton(
    value: String,
    placeholder: String,
    selectableMark: Boolean,
    onClick: () -> Unit
) {
    val textStyle = Typography.body1Regular

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .border(width = 1.dp, color = CS.Gray.G20, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (value.isEmpty()) placeholder else value,
            style = textStyle,
            color = if (value.isEmpty()) CS.Gray.G50 else CS.Gray.G90,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        if (selectableMark) {
            ArrowDownIcon(24.dp, 24.dp, tint = CS.Gray.G50, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun IconTextFieldOutlined(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .border(width = 1.dp, color = CS.Gray.G20, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SearchLineIcon(24.dp, 24.dp, tint = CS.Gray.G90)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = Typography.body1Regular, color = CS.Gray.G50)
        }
    }
}