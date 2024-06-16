plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.knee)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    androidNativeX64()
    androidNativeArm64()
    androidNativeX64()
    androidNativeArm64()

    linuxArm64().apply {
        compilations.getByName("main").cinterops {
            val pigpio by creating {
                packageName("pl.lemanski.pandamidi.gpio")

                headers = files(
                    project.file("src/nativeInterop/cinterop/pigpio.h").path
                )

                extraOpts("-staticLibrary", "libpigpio.so")
                extraOpts("-libraryPath", "$rootDir/libs/")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "pl.lemanski.pandamidi"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

knee {
    verboseLogs.set(true) // default: false
    verboseRuntime.set(true) // default: false
    verboseSources.set(true) // default: false
}
