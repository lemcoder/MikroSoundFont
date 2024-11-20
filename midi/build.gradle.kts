import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

group = "pl.lemanski.mikrosoundfont"
version = libs.versions.midi.get().toString()

android {
    namespace = "pl.lemanski.mikroSoundFont"
    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    jvmToolchain(17)

    androidTarget().apply {
        publishLibraryVariants("release")
    }

    mingwX64()
    linuxX64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {

        commonMain.dependencies {
            implementation(projects.soundFont)
            implementation(libs.kotlinx.io)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        getByName("androidInstrumentedTest").dependencies {
            implementation(libs.androidX.testRunner)
            implementation(libs.test.rules)
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}