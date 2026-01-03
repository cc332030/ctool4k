
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
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
    ?: getConfigValue("java.version")?.split(".")[0]
println("JDK_VERSION: $jdkVersion")

val isJdk8 = jdk8Str == jdkVersion

var rootProjectName = "ctool4k"
if(isJdk8) {
    rootProjectName += jdk8Str
}

rootProject.name = rootProjectName

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
        val logicalModuleName = ":$moduleName"

        include(logicalModuleName)
        val logicalModule = project(logicalModuleName)
        logicalModule.projectDir = moduleDir

    }
