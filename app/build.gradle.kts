import java.io.ByteArrayOutputStream
import java.util.Properties
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

var Prerelease = "Filmpire-Prerelease"
var InternalAPI = "Filmpire-Internal"
var Filmpire = "Filmpire"

android {
    namespace = "com.filmpire.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.filmpire.app"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "BUILD_TIME", "\"${System.currentTimeMillis()}\"")
        buildConfigField("boolean", "PRE_RELEASE", "false")
        buildConfigField("boolean", "BETA", "false")
        buildConfigField("String", "APPLICATION_ID", "\"com.filmpire.app\"")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            buildConfigField("String", "APPLICATION_ID", "\"com.filmpire.app.debug\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("stable") {
            dimension = "version"
            buildConfigField("boolean", "PRE_RELEASE", "false")
            buildConfigField("boolean", "BETA", "false")
            applicationIdSuffix = ""
        }
        create("prerelease") {
            dimension = "version"
            buildConfigField("boolean", "PRE_RELEASE", "true")
            buildConfigField("boolean", "BETA", "false")
            applicationIdSuffix = ".prerelease"
        }
        create("beta") {
            dimension = "version"
            buildConfigField("boolean", "PRE_RELEASE", "false")
            buildConfigField("boolean", "BETA", "true")
            applicationIdSuffix = ".beta"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }
}

dependencies {
    // CloudStream library
    implementation(project(":library"))
    
    // Pre-bundled extensions
    implementation(files("libs/Filmpire.cs3"))
    
    // Compose
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
    
    // TV
    implementation("androidx.tv:tv-foundation:1.0.0-alpha10")
    implementation("androidx.tv:tv-material:1.0.0-alpha10")
    
    // Leanback for Android TV
    implementation("androidx.leanback:leanback:1.0.0")
    
    // Player
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-dash:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    
    // Coil for images
    implementation("io.coil-kt:coil-compose:2.6.0")
    
    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
}
