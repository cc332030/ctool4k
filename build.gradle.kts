import com.c332030.ctool4k.gradle.buildsrc.util.configureSharedRepositories

fun getConfigValue(key: String): String? {
    return providers
        // 1. 系统属性（-D 参数）
        .systemProperty(key)
        // 2. 环境变量
        .orElse(providers.environmentVariable(key))
        // 3. gradle.properties
        .orElse(providers.gradleProperty(key))
        .getOrNull()
}

val jdkVersion = getConfigValue("jdk-version")

plugins {

    id("idea")

    alias(libs.plugins.maven.publish)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring) apply false

    //alias(libs.plugins.spring.boot2) apply false
    alias(libs.plugins.spring.boot4) apply false
    alias(libs.plugins.spring.dependency.management) apply false

}

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
