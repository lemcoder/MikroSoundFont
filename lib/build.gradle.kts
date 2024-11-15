import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.konan.util.DependencyDirectories.localKonanDir
import pl.lemanski.plugin.KonanPluginExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("pl.lemanski.plugin")
}

android {
    namespace = "pl.lemanski.mikrosoundfont"
    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    externalNativeBuild {
        cmake {
            path = file("src/androidMain/cpp/CMakeLists.txt")
        }
    }
}

kotlin {
    jvmToolchain(17)

    androidTarget().apply {
        publishAllLibraryVariants()
    }

    listOf(
        mingwX64(),
        linuxX64()


    ).forEach { target ->
        target.apply {
            val main by compilations.getting

            main.cinterops.create("libtsf") {
                definitionFile = File(rootDir, "native/libtsf.def")
                includeDirs.headerFilterOnly("$rootDir\\native\\include")
                extraOpts("-libraryPath", "$rootDir\\native\\lib\\${target.name}")
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

configure<KonanPluginExtension> {
    kotlinTarget = KonanTarget.LINUX_X64
    sourceDir = "${rootDir}/native/src"
    headerDir = "${rootDir}/native/include"
    libName = "tsf"
    konanPath = localKonanDir.listFiles()
        ?.first { it.name.contains(libs.versions.kotlin.get()) }?.absolutePath
}