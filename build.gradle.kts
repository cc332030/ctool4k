
import com.c332030.ctool4k.gradle.buildsrc.util.getConfigValue
import com.c332030.ctool4k.gradle.buildsrc.util.getJdkVersion
import com.c332030.ctool4k.gradle.buildsrc.util.getRequireConfigValue

plugins {

    id("idea")

    alias(libs.plugins.maven.publish)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)

}

val jdk8Str = "8"

val jdkVersion = getJdkVersion()
val isJdk8 = jdk8Str == jdkVersion

val versionStr = "0.0.1-SNAPSHOT"

group = "com.c332030"
version = versionStr
description = "CTool for Kotlin"

val isSnapshot = versionStr.endsWith("-SNAPSHOT")

val mavenCentral = getConfigValue("MAVEN_CENTRAL")
println("mavenCentral: $mavenCentral")

val nexusUsername = getConfigValue("NEXUS_USERNAME")
val nexusPassword = getConfigValue("NEXUS_PASSWORD")

val nexusSnapshotId = getConfigValue("NEXUS_SNAPSHOT_ID")
val nexusSnapshotUrl = getConfigValue("NEXUS_SNAPSHOT_URL")

val nexusReleaseId = getConfigValue("NEXUS_RELEASE_ID")
val nexusReleaseUrl = getConfigValue("NEXUS_RELEASE_URL")

val excludedAllProjects = listOf(
    ":ctool4k-dependencies",
)
allprojects {

    apply(plugin = "idea")
    apply(plugin = "maven-publish")

    val projectName = project.name

    idea {
        module { //println("name: ${name}, projectName: ${projectName}")
            name = projectName
        }
    }

    if (this.path in excludedAllProjects) {
        return@allprojects // 终止当前项目配置
    }

    val isJavax = projectName.endsWith("-javax")
    val isJakarta = projectName.endsWith("-jakarta")
    if (isJdk8 && isJakarta) {
        return@allprojects
    }

    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)

    val springBootVersion: String
    val springCloudVersion: String

    if (isJavax) {
        springBootVersion = rootProject.libs.versions.spring.boot2.get()
        springCloudVersion = rootProject.libs.versions.spring.cloud2021.get()
    } else if (isJdk8) {
        springBootVersion = rootProject.libs.versions.spring.boot2.get()
        springCloudVersion = rootProject.libs.versions.spring.cloud2021.get()
    } else {
        springBootVersion = rootProject.libs.versions.spring.boot4.get()
        springCloudVersion = rootProject.libs.versions.spring.cloud2025.get()
    }

    dependencies {

        implementation(platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"))

        compileOnly("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        testImplementation(rootProject.libs.jupiter.engine)
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    }

    java {
        withSourcesJar()
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xjsr305=strict", "-Xannotation-default-target=param-property"
            )
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    val enableJar = !projectName.endsWith("-pom")
    tasks.named<Jar>("jar") {
        enabled = enableJar
    }
    tasks.named<Jar>("sourcesJar") {
        enabled = enableJar
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }

        val nexusUsername = getRequireConfigValue("NEXUS_USERNAME")
        val nexusPassword = getRequireConfigValue("NEXUS_PASSWORD")

        val nexusId: String
        val nexusUrl: String
        if(isSnapshot) {
            nexusId = getRequireConfigValue("NEXUS_SNAPSHOT_ID")
            nexusUrl = getRequireConfigValue("NEXUS_SNAPSHOT_URL")
        } else{
            nexusId = getRequireConfigValue("NEXUS_RELEASE_ID")
            nexusUrl = getRequireConfigValue("NEXUS_RELEASE_URL")
        }

        repositories {
            maven {
                name = nexusId
                url = uri(nexusUrl)
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }

    }

}
