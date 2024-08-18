enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        includeBuild("plugin")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "PandaMidi"
include(":core")
include(":app")
include(":tinySoundFont")
