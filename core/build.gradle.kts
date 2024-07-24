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

        main.cinterops.create("tsf") {
            packageName = "org.tsf"
            extraOpts("-header", "$rootDir\\native\\include\\wrapper.h")
            extraOpts("-libraryPath", "$rootDir\\native\\lib")
            extraOpts("-staticLibrary", "libtsf.a")
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