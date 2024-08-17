plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral() // Add the Maven Central repository
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "pl.lemanski.kotlin.compile"
            implementationClass = "pl.lemanski.kotlin.compile.GreetingPlugin"
            version = "1.0.0"
        }
    }
}
