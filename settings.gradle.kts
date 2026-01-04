plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

fun getConfigValue(key: String): String? {

    val value = System.getProperty(key)
    if(!value.isNullOrEmpty()) {
        return value
    }
    return System.getenv(key)
}

val jdk8Str = "8"

val jdkVersion = getConfigValue("JDK_VERSION")
    ?: getConfigValue("java.version")
        ?.split('.')
        ?.firstOrNull()
println("jdkVersion: $jdkVersion")

val isJdk8 = jdk8Str == jdkVersion

rootProject.name = "ctool4k"

val baseDir = file(".")
baseDir.walk() // 递归遍历所有子目录
    .filter { dir ->
        dir.isDirectory
                && "buildSrc" != dir.name
                && !file("${dir.absolutePath}/settings.gradle.kts").exists()
                && file("${dir.absolutePath}/build.gradle.kts").exists()
    }
    .forEach { moduleDir ->

        val moduleName = moduleDir.name
        if(isJdk8 && moduleName.endsWith("-jakarta")) {
            return@forEach
        }

        val logicalModuleName = ":$moduleName"

        include(logicalModuleName)
        val logicalModule = project(logicalModuleName)
        logicalModule.projectDir = moduleDir

    }

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        val mavenCentralUrl = getConfigValue("MAVEN_CENTRAL")
        val nexusUsername = getConfigValue("NEXUS_USERNAME")
        val nexusPassword = getConfigValue("NEXUS_PASSWORD")
        val nexusSnapshotUrl = getConfigValue("NEXUS_SNAPSHOT_URL")
        val nexusReleaseUrl = getConfigValue("NEXUS_RELEASE_URL")

        mavenLocal()

        if (!mavenCentralUrl.isNullOrEmpty()) {
            maven {
                url = uri(mavenCentralUrl)
            }
        }

        if (!nexusSnapshotUrl.isNullOrEmpty()) {
            maven {
                url = uri(nexusSnapshotUrl)
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }

        if (!nexusReleaseUrl.isNullOrEmpty()) {
            maven {
                url = uri(nexusReleaseUrl)
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }

        mavenCentral()
        gradlePluginPortal()

    }

}
