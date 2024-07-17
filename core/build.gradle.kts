plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

kotlin {
    mingwX64().apply {
        val main by compilations.getting
        main.cinterops.create("fluidsynth") {
            packageName = "pl.lemanski.pandamidi"
            extraOpts("-staticLibrary", "libfluidsynth.a")
            extraOpts("-libraryPath", "$rootDir\\native\\lib")
            extraOpts("-header", "$rootDir\\native\\include\\fluidsynth.h")
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