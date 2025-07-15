package preset_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import colors.CS
import com.example.presentation.designsystem.typography.Typography

@Composable
fun SimpleTextField(
    value: String,
    readOnly: Boolean,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    val textStyle = Typography.body1Regular

    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        readOnly = readOnly,
        textStyle = textStyle.merge(
            TextStyle(
                color = if (value.isEmpty()) CS.Gray.G50 else CS.Gray.G90
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .drawBehind {
                val y = size.height - 1.dp.toPx()
                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = CS.Gray.G50,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        }
    )
}