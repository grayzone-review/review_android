import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
localProperties.load(FileInputStream(rootProject.file("local.properties")))
val kakaoNativeAppKey = localProperties["kakao_native_app_key"] as String
val kakaoRestAPIKey = localProperties["kakao_rest_api_key"] as String
val localStoreFile = localProperties["storeFilePath"] as String
val localStorePassword = localProperties["storePassword"] as String
val localKeyAlias = localProperties["keyAlias"] as String
val localKeyPassword = localProperties["keyPassword"] as String

plugins {
    id("com.android.application")               version "8.10.0"
    id("org.jetbrains.kotlin.android")          version "2.1.10"
    id("org.jetbrains.kotlin.plugin.compose")   version "2.1.10"
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = AppConfig.NameSpace.app
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(localStoreFile)
            storePassword = localStorePassword
            keyAlias = localKeyAlias
            keyPassword = localKeyPassword
        }
    }

    buildTypes {
        debug {
            buildConfigField("String","KAKAO_NATIVE_APP_KEY","\"$kakaoNativeAppKey\"")
            buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestAPIKey\"")
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Up for Debug")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String","KAKAO_NATIVE_APP_KEY","\"$kakaoNativeAppKey\"")
            buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestAPIKey\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.javaVersion
        targetCompatibility = AppConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":data:network"))
    implementation(project(":data:repository-implementation"))
    implementation(project(":domain:repository-interface"))
    implementation(project(":domain:entity"))
    implementation(project(":data:location"))
    implementation(project(":presentation:feature:company-detail"))
    implementation(project(":presentation:feature:search"))
    implementation(project(":presentation:feature:main"))
    implementation(project(":presentation:feature:login"))
    implementation(project(":presentation:feature:common"))
    implementation(project(":presentation:feature:archive"))
    implementation(project(":presentation:feature:onboarding"))
    implementation(project(":presentation:feature:mypage"))
    implementation(project(":common"))
    implementation(project(":presentation:design-system"))
    implementation(project(":data:storage"))
    implementation("com.google.android.material:material:1.12.0")

    implementation(Lottie.lottieCompose)
    implementation(Kakao.login)
    implementation(Kakao.map)
    implementation(DaggerHilt.hilt)
    implementation("com.google.android.gms:play-services-location:20.0.0")
    kapt(DaggerHilt.hiltCompiler)
    implementation(DaggerHilt.hiltNavigationCompose)
    implementation(Retrofit.retrofit)

    implementation(Deps.coreKtx)
    implementation(Deps.lifecycleRuntimeKtx)
    implementation(Deps.activityCompose)

    implementation(platform(Deps.composeBom))
    implementation(Deps.composeUi)
    implementation(Deps.composeUiGraphics)
    implementation(Deps.navigationCompose)
    implementation(Deps.composeUiToolingPreview)
    implementation(Deps.material3)

    debugImplementation(Deps.composeUiTooling)
    debugImplementation(Deps.composeUiTestManifest)
}