import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

android {
    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

kotlin {
    androidTarget().apply {
        publishAllLibraryVariants()
    }

    mingwX64()
    linuxX64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {

        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(projects.tinySoundFont)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}