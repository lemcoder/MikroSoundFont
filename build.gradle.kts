plugins {
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.maven.publish).apply(false)
    alias(libs.plugins.konan.plugin).apply(false)
}
