plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.presentation.onboarding"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:entity"))
    implementation(project(":domain:usecase"))
    implementation(project(":presentation:feature:common"))
    implementation(project(":presentation:design-system"))
    implementation(project(":data:location"))

    implementation(DaggerHilt.hilt)
    kapt(DaggerHilt.hiltCompiler)
    implementation(DaggerHilt.hiltNavigationCompose)
    implementation(Deps.coreKtx)
    implementation(Deps.navigationCompose)
    implementation(Deps.appcompat)
    implementation(Deps.material3)
}