
plugins {

    alias(libs.plugins.kotlin.jvm)
    `maven-publish`

}

val excludedAllProjects = listOf(":ctool4k-dependencies")
allprojects {

    if (this.path in excludedAllProjects) {
        return@allprojects // 终止当前项目配置
    }

    apply(from = "${rootProject.projectDir}/compile.gradle.kts")
    apply(from = "${rootProject.projectDir}/publish.gradle.kts")

}
