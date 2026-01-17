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
