plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    mingwX64().apply {
        binaries.executable {
            entryPoint = "main"
        }
    }

    sourceSets {
        mingwMain.dependencies {
            implementation(libs.kotlinx.io)
//            implementation(libs.pandamidi.core)
            implementation(projects.core)
        }
    }
}