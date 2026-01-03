
import com.c332030.ctool4k.gradle.buildsrc.util.configureSharedRepositories
import com.c332030.ctool4k.gradle.buildsrc.util.getConfigValue

plugins {

    fun getConfigValue(key: String): String? {

        val value = System.getProperty(key)
        if(!value.isNullOrBlank()) {
            return value
        }
        return System.getenv(key);
    }

    id("idea")

    alias(libs.plugins.maven.publish)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)

    val jdkVersion = getConfigValue("JDK_VERSION")
    if("8" == jdkVersion) {
        alias(libs.plugins.spring.boot2)
    } else {
        alias(libs.plugins.spring.boot4)
    }

}

val jdkVersion = getConfigValue("JDK_VERSION")

group = "com.c332030"
version = "0.0.1-SNAPSHOT"
description = "CTool for Kotlin"

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

    idea {
        module {
            //println("name: ${name}, project.name: ${project.name}")
            name = project.name
        }
    }

    repositories {
        configureSharedRepositories(this@allprojects)
    }

    if (this.path in excludedAllProjects) {
        return@allprojects // 终止当前项目配置
    }

    val isJavax = project.name.endsWith("-javax")
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)

    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.get().pluginId)

    if(isJavax) {
        apply(plugin = rootProject.libs.plugins.spring.boot2.get().pluginId)
    } else {
        apply(plugin = rootProject.libs.plugins.spring.boot4.get().pluginId)

        //dependencyManagement {
        //    imports {
        //        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.0}")
        //    }
        //}

    }

    dependencies {

        if (isJavax) {
            compileOnly(rootProject.libs.spring.boot2.web)
            testImplementation(rootProject.libs.spring.boot2.test)
        } else {
            compileOnly(rootProject.libs.spring.boot4.web)
            testImplementation(rootProject.libs.spring.boot4.test)
        }

        testImplementation(rootProject.libs.jupiter.engine)
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-Xannotation-default-target=param-property"
            )
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

}
