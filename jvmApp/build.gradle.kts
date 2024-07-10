plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

application.mainClass = "pl.lemanski.pandaloop.jvmApp.MainKt"

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    implementation(projects.core)
}