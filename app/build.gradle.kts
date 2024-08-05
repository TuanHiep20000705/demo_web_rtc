plugins {
    id(Dependencies.Kotlin.android)
    id(Dependencies.Kotlin.kapt)
    kotlin("kapt")
    id(Dependencies.Android.application)
    id(Dependencies.Kotlin.parcelize)
    id(Dependencies.Kotlin.jetbrains)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.demowebrtc"
    compileSdk = Versions.compileSdkVersion

    defaultConfig {
        applicationId = Versions.applicationId
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(Dependencies.Hilt.android)
    kapt(Dependencies.Hilt.kapt)
    kapt(Dependencies.Android.room)

    //lifecycle
    implementation(Dependencies.Lifecycle.runtimeKtx)
    implementation(Dependencies.Lifecycle.lifeCycle)
    implementation(Dependencies.Lifecycle.liveData)

    // Network
    implementation(Dependencies.Okhttp.okhttp)
    implementation(Dependencies.Okhttp.logging)
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.gson)

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")

    // webRtc
    implementation("com.mesibo.api:webrtc:1.0.5")
}