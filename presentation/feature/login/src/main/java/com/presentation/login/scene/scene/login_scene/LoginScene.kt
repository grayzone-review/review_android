package com.presentation.login.scene.scene.login_scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.team.common.feature_api.utility.Utility
import preset_ui.icons.KakaoBubble

@Composable
fun LoginScene(
    viewModel: LoginViewModel,
    appBarViewModel: AppBarViewModel
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        KakaoLoginButton(onClick =  { })
    }
}

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit
) {
    val kakaoYellow = Utility.hexToColor(hex = "#FEE500")
    val kakaoBrown = Utility.hexToColor(hex = "#3D1D1C")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = kakaoYellow,
                contentColor = kakaoBrown
            ),
        ) {
            KakaoBubble(24.dp, 24.dp)
            Spacer(Modifier.width(8.dp))
            Text(text = "카카오톡으로 시작하기", style = Typography.body1Bold, color = kakaoBrown)
        }
    }
}
