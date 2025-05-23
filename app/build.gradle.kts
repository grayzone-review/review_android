plugins {
    id("com.android.application")               version "8.10.0"
    id("org.jetbrains.kotlin.android")          version "2.0.21"
    id("org.jetbrains.kotlin.plugin.compose")   version "2.0.21"
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
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
}

dependencies {

    implementation(Deps.coreKtx)
    implementation(Deps.lifecycleRuntimeKtx)
    implementation(Deps.activityCompose)

    implementation(platform(Deps.composeBom))
    implementation(Deps.composeUi)
    implementation(Deps.composeUiGraphics)
    implementation(Deps.composeUiToolingPreview)
    implementation(Deps.material3)

    debugImplementation(Deps.composeUiTooling)
    debugImplementation(Deps.composeUiTestManifest)
}