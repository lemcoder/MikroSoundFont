repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
}

plugins {
    id("java-gradle-plugin")
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("myPlugin") {
            id = "pl.lemanski.plugin"
            implementationClass = "pl.lemanski.plugin.KonanPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
}