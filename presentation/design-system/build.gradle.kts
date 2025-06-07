plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = AppConfig.NameSpace.design_system
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = AppConfig.javaVersion
        targetCompatibility = AppConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":domain:entity"))
    implementation(project(":common"))

    implementation(DaggerHilt.hilt)
    implementation(Kakao.map)
    implementation("com.google.android.material:material:1.12.0")
    kapt(DaggerHilt.hiltCompiler)
    implementation(Deps.coreKtx)
    implementation(Deps.navigationCompose)
    implementation(Deps.appcompat)
    implementation(Deps.material3)

    // 바텀시트용 제거할때되면 제거하자!
    implementation("androidx.wear.compose:compose-material:1.4.1")
}