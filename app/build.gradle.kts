plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.castify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.castify"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Koin
    implementation(libs.bundles.koin)

    //Ktor
    implementation(libs.bundles.ktor)

    // extra material icons
    implementation(libs.androidx.material.icons.extended)

    // Material components optimized for TV apps
    implementation(libs.androidx.tv.material)

    // ViewModel in Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // JSON parser
    implementation(libs.kotlinx.serialization)

    // Media3
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // Compose Previews
    debugImplementation(libs.androidx.compose.ui.tooling)

    // gson
    implementation(libs.gson)

}