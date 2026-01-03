
plugins {

    `kotlin-dsl`

}

fun getConfigValue(key: String): String? {
    return providers.systemProperty(key) // 1. 系统属性（-D 参数）
        .orElse(providers.environmentVariable(key)) // 2. 环境变量
        .orElse(providers.gradleProperty(key))  // 3. gradle.properties
        .getOrNull()
}

val mavenCentral = getConfigValue("MAVEN_CENTRAL")

repositories {

    mavenLocal()

    if (!mavenCentral.isNullOrEmpty()) {
        maven {
            url = uri(mavenCentral)
        }
    }

    mavenCentral()
    gradlePluginPortal()

}
