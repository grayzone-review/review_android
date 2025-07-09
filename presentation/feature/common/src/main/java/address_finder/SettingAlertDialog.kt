package address_finder

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SettingAlertDialog(
    isShow: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    if (!isShow) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onOpenSettings() }) {
                Text("설정으로 이동")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        title = { Text("위치 권한 필요") },
        text = {
            Text("기능을 사용하려면 위치 권한이 필요합니다.\n" +
                    "설정 > 권한에서 위치를 허용해 주세요.")
        }
    )
}