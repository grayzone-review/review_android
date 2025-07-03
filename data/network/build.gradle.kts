import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))
val kakaoNativeAppKey = localProperties["kakao_native_app_key"] as String
val kakaoRestAPIKey = localProperties["kakao_rest_api_key"] as String

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = AppConfig.NameSpace.network
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String","KAKAO_NATIVE_APP_KEY","\"$kakaoNativeAppKey\"")
            buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestAPIKey\"")
        }

        release {
            isMinifyEnabled = false
            buildConfigField("String","KAKAO_NATIVE_APP_KEY","\"$kakaoNativeAppKey\"")
            buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestAPIKey\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data:dto"))
    implementation(Deps.coreKtx)

    implementation(DaggerHilt.hilt)
    kapt(DaggerHilt.hiltCompiler)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.gsonConverter)
}