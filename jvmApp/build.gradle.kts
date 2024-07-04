plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

application.mainClass = "pl.lemanski.pandaloop.jvmApp.MainKt"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.core)
}