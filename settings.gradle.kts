import com.c332030.ctool4k.gradle.buildsrc.util.getConfigValue

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

val jdk8Str = "8"
extra["jdk8Str"] = jdk8Str

val jdkVersion = getConfigValue("JDK_VERSION")
println("JDK_VERSION: $jdkVersion")
extra["jdkVersion"] = jdkVersion

val isJdk8 = jdk8Str == jdkVersion
extra["isJdk8"] = isJdk8

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
