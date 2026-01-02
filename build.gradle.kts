import org.gradle.internal.os.OperatingSystem

plugins {

    id("idea")

    alias(libs.plugins.maven.publish)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring) apply false

    alias(libs.plugins.spring.boot2) apply false
    alias(libs.plugins.spring.boot4) apply false
    alias(libs.plugins.spring.dependency.management) apply false

}

group = "com.c332030"
version = "0.0.1-SNAPSHOT"
description = "CTool for kotlin"

val excludedAllProjects = listOf(":ctool4k-dependencies")
allprojects {

    apply(plugin = "idea")
    apply(plugin = "maven-publish")

    idea {
        module {
            //println("name: ${name}, project.name: ${project.name}")
            name = project.name
        }
    }

    if (this.path in excludedAllProjects) {
        return@allprojects // 终止当前项目配置
    }

    repositories {

        maven {
            url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
        }
        mavenCentral()

    }

    val isJavax = project.name.endsWith("-javax")
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)

    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.get().pluginId)

    if(isJavax) {
        apply(plugin = rootProject.libs.plugins.spring.boot2.get().pluginId)
    } else {
        apply(plugin = rootProject.libs.plugins.spring.boot4.get().pluginId)

        dependencyManagement {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.0}")
            }
        }

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
