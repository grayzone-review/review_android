package common_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun UpIndicator(
    isShow: Boolean,
    needDim: Boolean = false
) {
    if (!isShow) return           // 표시 안 할 땐 조용히 빠짐

    // ───────── Lottie 준비 ─────────
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.presentation.design_system.R.raw.insider_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (needDim)
                    Modifier.background(Color(0x80000000))
                else Modifier
            )
            .pointerInput(Unit) { }
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
//                .align(Alignment.Center)
        )
    }
}