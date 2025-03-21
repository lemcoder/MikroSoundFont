import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.konan.util.DependencyDirectories.localKonanDir
import io.github.lemcoder.KonanPluginExtension


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.konan.plugin)
    signing
}

val s: String = File.separator

group = "io.github.lemcoder.mikrosoundfont"
version = libs.versions.lib.get()

android {
    namespace = "io.github.lemcoder.mikrosoundfont"
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
        publishLibraryVariants("release")
    }

    buildList {
        add(mingwX64())
        add(linuxX64())
        if (System.getProperty("os.name").lowercase().contains("mac")) {
            add(iosArm64())
            add(iosSimulatorArm64())
            add(iosX64())
            add(macosArm64())
            add(macosX64())
        }
    }.forEach { target ->
        target.apply {
            val main by compilations.getting

            main.cinterops.create("libtsf") {
                definitionFile = File(rootDir, "native${s}libtsf.def")
                includeDirs.headerFilterOnly("$rootDir${s}native${s}include")
                extraOpts("-libraryPath", "$rootDir${s}native${s}lib${s}${target.konanTarget.name}")
            }
        }
    }

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
    targets = buildList {
        add(KonanTarget.LINUX_X64)
        add(KonanTarget.MINGW_X64)
        if (System.getProperty("os.name").lowercase().contains("mac")) {
            add(KonanTarget.IOS_ARM64)
            add(KonanTarget.IOS_SIMULATOR_ARM64)
            add(KonanTarget.IOS_X64)
            add(KonanTarget.MACOS_X64)
            add(KonanTarget.MACOS_ARM64)
        }
    }
    sourceDir = "${rootDir}/native/src"
    headerDir = "${rootDir}/native/include"
    outputDir = "${rootDir}/native/lib"
    libName = "tsf"
    konanPath = localKonanDir.listFiles()?.first {
        it.name.contains(libs.versions.kotlin.get())
    }?.absolutePath
}

// Publishing

publishing {
    repositories {
        mavenLocal()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    coordinates(
        groupId = group.toString(),
        artifactId = "soundfont",
        version = version.toString()
    )

    println(SonatypeHost.DEFAULT.toString())

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