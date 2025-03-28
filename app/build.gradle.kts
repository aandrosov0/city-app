plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "aandrosov.city.app"

    compileSdk = 35

    defaultConfig {
        applicationId = "aandrosov.city.app"

        minSdk = 28
        targetSdk = 35

        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.13.1")
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.navigation)
    implementation(libs.compose.koin.navigation)
    implementation(libs.compose.coil)

    implementation(libs.coil.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    implementation(libs.serialization.json)

    implementation(project(":data"))
}