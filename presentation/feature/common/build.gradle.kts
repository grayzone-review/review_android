plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.presentation.common"
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
    buildFeatures {
        compose = true
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
    implementation(project(":presentation:design-system"))
    implementation(project(":domain:usecase"))
    implementation(project(":data:location"))
    implementation(project(":data:storage"))

    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(Deps.coroutinesPlayServices)

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(Location.permission)
    implementation(Location.gmsLocationService)
    implementation(DaggerHilt.hilt)
    implementation("androidx.media3:media3-common-ktx:1.7.1")
    kapt(DaggerHilt.hiltCompiler)
    implementation(DaggerHilt.hiltNavigationCompose)
    implementation(Deps.coreKtx)
    implementation(Deps.navigationCompose)
    implementation(Deps.appcompat)
    implementation(Deps.material3)
}