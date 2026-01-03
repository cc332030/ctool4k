package com.c332030.ctool4k.gradle.buildsrc.util

import com.c332030.ctool4k.gradle.buildsrc.constant.JAVA_VERSION
import com.c332030.ctool4k.gradle.buildsrc.constant.JDK_VERSION
import org.gradle.api.Project

fun Project.getConfigValue(key: String): String? {
    return providers.systemProperty(key) // 1. 系统属性（-D 参数）
        .orElse(providers.environmentVariable(key)) // 2. 环境变量
        .orElse(providers.gradleProperty(key))  // 3. gradle.properties
        .getOrNull()
}

fun Project.getRequireConfigValue(key: String): String {
    return getConfigValue(key)
        ?: throw IllegalArgumentException("$key is required")
}

fun Project.getJdkVersion(): String? {
    return getConfigValue(JDK_VERSION)
        ?: getConfigValue(JAVA_VERSION)
            ?.split('.')
            ?.firstOrNull()
}
