buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        // Add the KSP plugin classpath - ensure version matches your Kotlin version
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.22-1.0.17") // <-- ADDED
    }
}

// This 'plugins' block is an alternative, more modern way to declare project-level plugins
// If you switch to this, you can remove the 'buildscript' block entirely.
// plugins {
//     id("com.android.application") version "8.2.2" apply false
//     id("org.jetbrains.kotlin.android") version "1.9.22" apply false
//     id("com.google.gms.google-services") version "4.4.1" apply false
//     id("com.google.dagger.hilt.android") version "2.51.1" apply false
//     id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false // <-- ADDED KSP plugin ID
// }

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}