package common_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import colors.CS
import com.example.presentation.designsystem.typography.Typography

@Composable
fun UpAlertIconDialog(
    icon: (@Composable () -> Unit)?,
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(CS.Gray.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /* 아이콘 */
                icon?.let { bindIcon ->
                    Spacer(Modifier.height(23.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(CS.PrimaryOrange.O40),
                        contentAlignment = Alignment.Center
                    ) { bindIcon() }
                    Spacer(Modifier.height(8.dp))
                }
                /* 제목 */
                Text(text = title, style = Typography.h3, color = CS.Gray.G90, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                /* 메시지 */
                Text(text = message, style = Typography.body2Regular, color = CS.Gray.G70, textAlign = TextAlign.Center)
                icon?.let { Spacer(Modifier.height(23.dp)) } // 아이콘 조건부 패딩
            }

            /* ── 하단 두 버튼 ─────────────────────────────────── */
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                // 취소 (좌측)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(CS.Gray.G10)
                        .clickable { onCancel() }
                        .drawBehind {
                            val stroke = 1.dp.toPx()
                            drawLine(
                                color = CS.Gray.G20,
                                start = Offset(0f, 0f),
                                end   = Offset(size.width, 0f),
                                strokeWidth = stroke
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = cancelText, style = Typography.body1Regular, color = CS.Gray.G50)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(CS.PrimaryOrange.O40)
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = confirmText, style = Typography.body1Regular, color = CS.Gray.White)
                }
            }
        }
    }
}