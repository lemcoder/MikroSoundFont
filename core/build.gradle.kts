import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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

        main.cinterops.create("libtsf") {
            definitionFile = File(rootDir, "native/libtsf.def")
            includeDirs.headerFilterOnly("$rootDir\\native\\include")
            extraOpts("-libraryPath", "$rootDir\\native\\lib")
        }
    }

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

publishing {
    repositories {
        mavenLocal()
    }
}