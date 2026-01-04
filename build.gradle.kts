
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
val isJdk8 = jdk8Str == jdkVersion.toString()

val rootProjectName = rootProject.name
val gitProjectName = rootProjectName.split("-").first()

val versionStr = "0.0.1-SNAPSHOT"

val author = "c332030"
val authorGroup = "c${author}"
val authorEmail = "${author}@${author}.com"

val repoDomain = "github.com"

val authorGroupPath = "${repoDomain}/${authorGroup}"
val authorGroupUrl = "https://${authorGroupPath}"

val repoPath = "${authorGroupPath}/${gitProjectName}"
val repoUrl = "https://${repoPath}"

group = "com.${author}.${rootProjectName}"
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

val dependenciesModule = "ctool4k-dependencies"

val pomModules = listOf(
    rootProjectName,
    dependenciesModule,
    "ctool4k-parent",
    "ctool4k-component",
)

fun isPom(projectName: String): Boolean {
    return pomModules.contains(projectName)
            || projectName.endsWith("-pom")
}

allprojects {

    val libs = rootProject.libs
    val versions = libs.versions
    val plugins = libs.plugins

    apply(plugin = "idea")
    apply(plugin = "maven-publish")

    group = rootProject.group
    version = rootProject.version

    val projectName = project.name

    idea {
        module {
            //println("name: ${name}, projectName: ${projectName}")
            name = projectName
        }
    }

    val isPom = isPom(projectName)
    val enableJar = !isPom

    //println("projectName: $projectName, isPom: $isPom")
    if(dependenciesModule == projectName) {
        apply(plugin = "java-platform")
    } else {
        apply(plugin = plugins.kotlin.jvm.get().pluginId)
        tasks.named<Jar>("jar") {
            enabled = enableJar
        }
    }


    val nexusUsername = getConfigValue("NEXUS_USERNAME")
    if(!nexusUsername.isNullOrEmpty()) {
        publishing {

            publications {

                val publishName = if(isPom) {
                    "mavenPom"
                } else {
                    "mavenJava"
                }
                create<MavenPublication>(publishName) {

                    pom {

                        if(isPom) {
                            packaging = "pom"
                        }

                        //name.set("CTool4K Dependencies")
                        //description.set("Bill of Materials for CTool4K project - manages all dependency versions")
                        url.set(repoUrl)

                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                                distribution.set("repo")
                            }
                        }

                        developers {
                            developer {
                                id.set(author)
                                name.set(author)
                                email.set(authorEmail)
                                organization.set(authorGroup)
                                organizationUrl.set(authorGroupUrl)
                            }
                        }
                        scm {
                            connection.set("scm:git:git://${repoPath}.git")
                            developerConnection.set("scm:git:ssh://${repoPath}.git")
                            url.set(repoUrl)
                        }
                    }

                    if(dependenciesModule == projectName) {
                        from(components["javaPlatform"])
                    }
                    if(!isPom) {
                        from(components["kotlin"])
                    }
                }

            }

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
                    // 影响 idea 侧边栏名称
                    //name = nexusId
                    url = uri(nexusUrl)
                    credentials {
                        username = nexusUsername
                        password = nexusPassword
                    }
                }
            }

        }
    }

    if (isPom) {
        return@allprojects // 终止当前项目配置
    }

    val isJavax = projectName.endsWith("-javax")
    val isJakarta = projectName.endsWith("-jakarta")
    if (isJdk8 && isJakarta) {
        return@allprojects
    }

    apply(plugin = plugins.kotlin.spring.get().pluginId)

    val springBootVersion: String
    val springCloudVersion: String

    if (isJavax) {
        springBootVersion = versions.spring.boot2.get()
        springCloudVersion = versions.spring.cloud2021.get()
    } else if (isJdk8) {
        springBootVersion = versions.spring.boot2.get()
        springCloudVersion = versions.spring.cloud2021.get()
    } else {
        springBootVersion = versions.spring.boot4.get()
        springCloudVersion = versions.spring.cloud2025.get()
    }

    dependencies {

        implementation(platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"))

        compileOnly("org.springframework.boot:spring-boot-starter-web")

        compileOnly(libs.hutool)

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation(libs.jupiter.engine)
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    }

    java {
        if(enableJar) {
            withSourcesJar()
        }
    }

    kotlin {
        jvmToolchain{
            languageVersion = JavaLanguageVersion.of(jdkVersion)
            vendor = JvmVendorSpec.IBM
        }
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xjsr305=strict", "-Xannotation-default-target=param-property"
            )
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

}
