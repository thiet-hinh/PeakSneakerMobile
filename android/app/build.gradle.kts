plugins {

    id("com.android.application")

    id("org.jetbrains.kotlin.android")

    id("org.jetbrains.kotlin.plugin.compose")

    id("com.google.devtools.ksp")

    id("com.google.dagger.hilt.android")

    id("com.google.gms.google-services")
}

android {

    namespace = "com.example.shoeshop"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.shoeshop"

        minSdk = 23

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_21

        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {

        jvmTarget = "21"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")

    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    implementation("androidx.activity:activity-compose:1.10.1")

    implementation(platform("androidx.compose:compose-bom:2025.02.00"))

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.material3:material3")

    implementation("androidx.compose.ui:ui-tooling-preview")

    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.navigation:navigation-fragment-ktx:2.8.6")

    implementation("androidx.navigation:navigation-ui-ktx:2.8.6")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // ROOM
    implementation("androidx.room:room-runtime:2.7.0")

    implementation("androidx.room:room-ktx:2.7.0")

    ksp("androidx.room:room-compiler:2.7.0")

    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // HILT
    implementation("com.google.dagger:hilt-android:2.56.2")

    ksp("com.google.dagger:hilt-compiler:2.56.2")

    // FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))

    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-messaging")

    implementation("com.google.firebase:firebase-analytics")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
}