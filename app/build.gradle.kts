plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Correct alias from libs.versions.toml
    alias(libs.plugins.hilt) // Correct alias from libs.versions.toml
    alias(libs.plugins.google.services) // Correct alias from libs.versions.toml
    // Consider adding kotlin-parcelize if needed: alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.evav3"
    compileSdk = 34 // Keep this updated

    defaultConfig {
        applicationId = "com.example.evav3"
        minSdk = 26
        targetSdk = 34 // Keep this updated
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Enable for release: isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Reference the javaVersion defined in libs.versions.toml
        sourceCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
    }

    kotlinOptions {
        // Reference the javaVersion defined in libs.versions.toml
        jvmTarget = libs.versions.javaVersion.get()
    }

    buildFeatures {
        viewBinding = true
        // dataBinding = true // Enable if you use Data Binding
    }
}

dependencies {
    // Core Android & Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Correct alias
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Lifecycle Components
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Firebase (using BOM)
    implementation(platform(libs.firebase.bom)) // Correct alias
    implementation(libs.firebase.firestore.ktx) // Correct alias
    implementation(libs.firebase.auth.ktx)    // Correct alias

    // Networking
    implementation(libs.retrofit.core) // Correct alias
    implementation(libs.retrofit.converter.gson) // Correct alias
    implementation(libs.okhttp.logging.interceptor) // Correct alias

    // UI Components
    implementation(libs.androidx.recyclerview) // Correct alias (added to libs.versions.toml)
    implementation(libs.markwon.core)
    implementation(libs.markwon.ext.strikethrough)
    implementation(libs.markwon.ext.tables)
    implementation(libs.markwon.html)

    // Security
    implementation(libs.androidx.security.crypto)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Hilt (Dependency Injection)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Use ksp, correct alias

    // Room (Persistence Library)
    implementation(libs.androidx.room.runtime) // Correct alias
    implementation(libs.androidx.room.ktx)    // Correct alias
    ksp(libs.androidx.room.compiler) // Use ksp, correct alias

    // Timber (Logging)
    implementation(libs.timber) // Correct alias (added to libs.versions.toml)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit) // Correct alias
    androidTestImplementation(libs.androidx.test.espresso.core) // Correct alias
}
