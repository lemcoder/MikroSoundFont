plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("maven-publish")
}

group = "pl.lemanski.pandamidi"
version = "0.0.1"

kotlin {
    mingwX64().apply {
        val main by compilations.getting

        main.compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xbinary=gc=noop")
            }
        }

        main.cinterops.create("libmwc") {
            definitionFile = File(rootDir, "native/libmwc.def")
            includeDirs.headerFilterOnly("$rootDir\\native\\include")
            extraOpts("-libraryPath", "$rootDir\\native\\lib")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}