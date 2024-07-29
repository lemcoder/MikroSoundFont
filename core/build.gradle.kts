plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    mingwX64().apply {
        val main by compilations.getting

        main.cinterops.create("mwc") {
            packageName = "pl.lemanski.mwc"
            extraOpts("-header", "$rootDir\\native\\include\\midi_wav_converter.h")
            extraOpts("-libraryPath", "$rootDir\\native\\lib")
            extraOpts("-staticLibrary", "libmwc.a")
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