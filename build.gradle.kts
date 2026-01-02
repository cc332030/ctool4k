
plugins {

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.maven.publish)

}

val excludedAllProjects = listOf(":ctool4k-dependencies")
allprojects {

    apply(plugin = "maven-publish")
    if (this.path in excludedAllProjects) {
        return@allprojects // 终止当前项目配置
    }

    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    repositories {
        mavenCentral()
    }

    dependencies {

    }

}
