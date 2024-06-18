plugins {
    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.kotlinJvm)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}