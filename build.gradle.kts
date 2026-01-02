
plugins {

    id("idea")

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.maven.publish)

}

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

    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    repositories {
        mavenCentral()
    }

    dependencies {

    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

}
