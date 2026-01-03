
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

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
        val logicalModuleName = ":$moduleName"

        include(logicalModuleName)
        val logicalModule = project(logicalModuleName)
        logicalModule.projectDir = moduleDir

    }
