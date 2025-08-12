package edit_profile_address_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.LegalDistrictInfo
import com.example.presentation.designsystem.typography.Typography
import preset_ui.SimpleTextFieldOutlinedButton
import preset_ui.icons.SignUpRemove

@Composable
fun NicknameInput(
    nicknameField: NickNameField,
    enabledCheckDuplicateButton: Boolean = true,
    onValueChange: (String) -> Unit,
    onCheckDuplicate: () -> Unit,
) {
    val invalidRegex = remember { Regex("[^가-힣ㄱ-ㅎa-zA-Z0-9]") }
    val textColor = when (nicknameField.fieldState) {
        FieldState.ClientError -> CS.System.Red
        else -> CS.Gray.G90
    }
    val borderColor = when (nicknameField.fieldState) {
        FieldState.ClientError -> CS.System.Red
        else -> CS.Gray.G20
    }
    val errorMessageColor = when (nicknameField.fieldState) {
        FieldState.ClientError -> CS.System.Red
        FieldState.Normal -> CS.Gray.G50
        FieldState.ServerSuccess -> CS.System.Blue
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(text = "닉네임", style = Typography.h3, color = CS.Gray.G90)
        Spacer(Modifier.height(10.dp))
        BasicTextField(
            value = nicknameField.value,
            onValueChange = {
                val filtered = it
                    .replace(invalidRegex, "")
                    .take(12)
                onValueChange(filtered)
            },
            singleLine = true,
            textStyle = Typography.body1Regular.copy(color = textColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        if (nicknameField.value.isEmpty()) {
                            Text(text = "닉네임", style = Typography.body1Regular, color = CS.Gray.G40)
                        }
                        innerTextField()
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = onCheckDuplicate,
                        enabled = enabledCheckDuplicateButton,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 5.5.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CS.PrimaryOrange.O40,
                            contentColor = CS.Gray.White,
                            disabledContentColor = CS.Gray.White,
                            disabledContainerColor = CS.PrimaryOrange.O20
                        ),
                        elevation = null
                    ) {
                        Text("중복 확인", style = Typography.body2Regular)
                    }
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        Text(text = nicknameField.errorMessage, style = Typography.captionRegular, color = errorMessageColor)
    }
}

@Composable
fun MyTownInput(
    value: LegalDistrictInfo?,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        val dong = value?.name
            ?.split(" ")
            ?.getOrNull(2)
            ?: ""

        Text(text = "우리 동네 설정", style = Typography.h3, color = CS.Gray.G90)
        Spacer(Modifier.height(10.dp))
        SimpleTextFieldOutlinedButton (
            value = dong,
            placeholder = "동 검색하기",
            selectableMark = false,
            onClick = { onClick(dong) }
        )
    }
}

@Composable
fun InterestInput(
    legalDistrictInfos: List<LegalDistrictInfo>,
    onRemoveButtonClick: (LegalDistrictInfo) -> Unit,
    onAddTownButtonClick: () -> Unit
) {
    Column {
        Text(text = "관심 동네 설정 (선택)", style = Typography.h3, color = CS.Gray.G90, modifier = Modifier
            .padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(18.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(legalDistrictInfos, key = { it }) { legalDistrictInfo ->
                val dong = legalDistrictInfo.name.split(" ")[2]

                DeletableTownChip(
                    text = dong,
                    onDelete = { onRemoveButtonClick(legalDistrictInfo) }
                )
            }

            if (legalDistrictInfos.size < 3) {
                item { AddTownChip(onClick = { onAddTownButtonClick() }) }
            }
        }
    }
}

// MARK: - Private
@Composable
private fun DeletableTownChip(
    text: String,
    onDelete: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .height(48.dp)
                .border(1.dp, CS.PrimaryOrange.O40, RoundedCornerShape(8.dp))
                .background(CS.PrimaryOrange.O10, RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, style = Typography.body1Bold, color = CS.PrimaryOrange.O40)
        }
        // ② 삭제 아이콘 ― 칩과 형제이므로 border 위에 자연스럽게 올라감
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopEnd)
                .offset(x = 5.dp, y = (-5).dp)
        ) {
            SignUpRemove(18.dp, 18.dp)
        }
    }
}

@Composable
private fun AddTownChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White
        ),
        elevation = null,
        modifier = modifier.height(48.dp)
    ) {
        Text("추가하기", style = Typography.body2Bold)
    }
}