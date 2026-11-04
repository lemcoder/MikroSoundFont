import io.github.lemcoder.kotlin.of
import io.github.lemcoder.model.NativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.konan.plugin)
    signing
}

group = "io.github.lemcoder.mikrosoundfont"
version = libs.versions.lib.get()

val isMacHost = System.getProperty("os.name").lowercase().contains("mac")

kotlin {
    jvmToolchain(17)

    android {
        namespace = "io.github.lemcoder.mikrosoundfont"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        // commonTest also runs on a connected device: ./gradlew :soundfont:connectedAndroidDeviceTest
        withDeviceTestBuilder { sourceSetTreeName = "test" }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    mingwX64()
    linuxX64()
    if (isMacHost) {
        iosArm64()
        iosSimulatorArm64()
        iosX64()
        macosArm64()
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidDeviceTest") {
            resources.srcDir("src/commonTest/resources")
            dependencies {
                implementation(libs.androidx.test.runner)
            }
        }
    }
}

konan {
    config { konanHome.set(kotlinNativeDistribution()) }

    // TinySoundFont (native/src/tsf.c), built for every Kotlin/Native target and every Android ABI.
    val tsf = libraries.create("tsf") {
        sourceDirectory.set(rootProject.layout.projectDirectory.dir("native/src"))
        targets.set(
            kotlin.targets.withType<KotlinNativeTarget>().map { NativeTarget.of(it.konanTarget) } +
                NativeTarget.android
        )
    }

    // One binding for both sides: KGP's cinterop on Kotlin/Native, generated JNI bindings on Android.
    bindings {
        create("tsf") {
            defFile.set(rootProject.file("native/libtsf.def"))
            includeDirs.from(rootProject.file("native/include"))
            library(tsf)
        }
    }
}

// The native test binaries read their fixtures from here (see readTestResource)
tasks.withType<KotlinNativeTest>().configureEach {
    environment("MIKROSOUNDFONT_TEST_RESOURCES", file("src/commonTest/resources").absolutePath)
}

// Publishing

publishing {
    repositories {
        mavenLocal()
    }
}

mavenPublishing {
    publishToMavenCentral()

    coordinates(
        groupId = group.toString(),
        artifactId = "soundfont",
        version = version.toString()
    )

    pom {
        name.set("MikroSoundFont")
        description.set("Kotlin Multiplatform library for sample-based synthesis to play MIDI files.")
        inceptionYear.set("2024")
        url.set("https://github.com/lemcoder/MikroSoundFont")

        licenses {
            license {
                name.set("Apache-2.0 license")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }

        developers {
            developer {
                id.set("lemcoder")
                name.set("Mikołaj Lemański")
                email.set("lemanski.dev@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/lemcoder/MikroSoundFont")
        }
    }
}

// Sign with default plugin
signing {
    useInMemoryPgpKeys(
        System.getenv("SIGNING_KEY"),
        System.getenv("SIGNING_KEY_PASSWORD")
    )
    sign(publishing.publications)

    // Temporary workaround, see https://github.com/gradle/gradle/issues/26091#issuecomment-1722947958
    tasks.withType<AbstractPublishToMaven>().configureEach {
        val signingTasks = tasks.withType<Sign>()
        mustRunAfter(signingTasks)
    }
}