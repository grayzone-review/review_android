
object Versions {
    // ── AndroidX & Compose ─────────────────────────────
    const val core                = "1.16.0"
    const val lifecycleRuntimeKtx = "2.9.0"
    const val activityCompose     = "1.10.1"
    const val composeBom          = "2024.09.00"
    const val material3           = "1.3.2"

    // ── Navigation (Compose) ───────────────────────────
    const val navigationCompose   = "2.9.0"

    // ── UI (XML) ───────────────────────────
    const val appcompat           = "1.7.0"

    // ── Retrofit ─────────────────────────────
    const val retrofit            = "3.0.0"
    const val gsonConverter       = "3.0.0"
    const val okHttp              = "4.12.0"

    // ── Hilt ─────────────────────────────
    const val hilt                  = "2.56.2"
    const val hiltNavigationCompose = "1.2.0"

    // ── Kakao ─────────────────────────────
    const val map                   = "2.12.8"
    const val login                 = "2.21.4"

    // ── Coroutines ─────────────────────────────
    const val coroutineCore       = "1.10.2"

    // ── Location ─────────────────────────────
    const val gmsLocationService  = "21.3.0"
    const val permission          = "0.37.3"
    // ── Lottie ─────────────────────────────
    const val lottieCompose       = "6.6.6"
}

object Deps {
    // --- Core & Lifecycle ---
    const val coreKtx              = "androidx.core:core-ktx:${Versions.core}"
    const val lifecycleRuntimeKtx  = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"

    // --- Activity + Compose ---
    const val activityCompose      = "androidx.activity:activity-compose:${Versions.activityCompose}"

    // --- Compose BOM (platform) ---
    const val composeBom           = "androidx.compose:compose-bom:${Versions.composeBom}"

    // --- Compose UI 계열 (버전은 BOM으로 통일) ---
    const val composeUi                = "androidx.compose.ui:ui"
    const val composeUiGraphics        = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview  = "androidx.compose.ui:ui-tooling-preview"
    const val composeUiTooling         = "androidx.compose.ui:ui-tooling"
    const val composeUiTestManifest    = "androidx.compose.ui:ui-test-manifest"

    // --- Material3 (버전 역시 BOM으로 통일) ---
    const val material3            = "androidx.compose.material3:material3:${Versions.material3}"

    // --- Navigation Compose ---
    const val navigationCompose        = "androidx.navigation:navigation-compose:${Versions.navigationCompose}"

    // --- UI(View) 계열 ---
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    // --- Coroutines 계열 ---
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutineCore}"
    const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutineCore}"
}

object Retrofit {
    const val retrofit                  = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter             = "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"
    const val okHttp                    = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
}

object DaggerHilt {
    const val dagger                    = "com.google.dagger:dagger-android:2.x"
    const val hilt                      = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler              = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltNavigationCompose     = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"
}

object Kakao {
    const val map                       = "com.kakao.maps.open:android:${Versions.map}"
    const val login                     = "com.kakao.sdk:v2-user:${Versions.login}"
}

object Location {
    const val gmsLocationService        = "com.google.android.gms:play-services-location:${Versions.gmsLocationService}"
    const val permission                = "com.google.accompanist:accompanist-permissions:${Versions.permission}"
}

object Lottie {
    const val lottieCompose             = "com.airbnb.android:lottie-compose:${Versions.lottieCompose}"
}