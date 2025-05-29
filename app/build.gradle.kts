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
    implementation(project(":data:network"))
    implementation(project(":data:repository-implementation"))
    implementation(project(":presentation:feature:company-detail"))
    implementation(project(":common"))
    implementation(project(":presentation:design-system"))

    implementation("com.kakao.maps.open:android:2.12.8")

    implementation(DaggerHilt.hilt)
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