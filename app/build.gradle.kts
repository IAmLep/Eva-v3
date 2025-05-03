plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt") // Kotlin Annotation Processing Tool
    id("com.google.gms.google-services") // Firebase services
    id("dagger.hilt.android.plugin") // Hilt dependency injection
}

android {
    namespace = "com.example.evav3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.evav3"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Consider enabling for release builds later
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
        viewBinding = true // Enables View Binding
    }
}

dependencies {
    // Define versions in one place
    val room_version = "2.6.1"
    val hilt_version = "2.51.1"
    val lifecycle_version = "2.7.0"
    val navigation_version = "2.7.7"
    val coroutines_version = "1.7.3"
    val firebase_bom_version = "32.8.0"
    val retrofit_version = "2.9.0"
    val okhttp_logging_version = "4.11.0"
    val markwon_version = "4.6.2"
    val timber_version = "5.0.1"

    // Core Android & Kotlin
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Lifecycle Components (ViewModel, LiveData)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:$firebase_bom_version"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Networking (Retrofit, OkHttp)
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_logging_version")

    // UI Components (RecyclerView, Markdown)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("io.noties.markwon:core:$markwon_version")
    implementation("io.noties.markwon:ext-strikethrough:$markwon_version")
    implementation("io.noties.markwon:ext-tables:$markwon_version")
    implementation("io.noties.markwon:html:$markwon_version")

    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06") // Consider using a stable version if available

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")

    // Room (Persistence Library)
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Kotlin Extensions and Coroutines support
    kapt("androidx.room:room-compiler:$room_version")     // Annotation processor using Kapt

    // Timber (Logging)
    implementation("com.jakewharton.timber:timber:$timber_version")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Kapt configuration
kapt {
    correctErrorTypes = true
}