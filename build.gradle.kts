

plugins {
    alias(libs.plugins.kotlin.jvm)
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


allprojects {

    alias()

    apply(plugin = "java-library")
    apply(plugin = libs.plugins.kotlin.jvm)

    repositories {
        mavenCentral()
    }

    dependencies {

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation(libs.junit.jupiter.engine)

        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    }

    tasks.named<Test>("test") {
        // Use JUnit Platform for unit tests.
        useJUnitPlatform()
    }

}
