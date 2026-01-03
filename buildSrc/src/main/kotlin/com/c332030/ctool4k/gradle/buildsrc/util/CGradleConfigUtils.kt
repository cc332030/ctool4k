package com.c332030.ctool4k.gradle.buildsrc.util

import org.gradle.api.Project

fun getConfigValue(key: String): String? {

    val value = System.getProperty(key)
    if(!value.isNullOrEmpty()) {
        return value
    }

    return System.getenv(key)
}

fun Project.getConfigValue(key: String): String? {
    return providers.systemProperty(key) // 1. 系统属性（-D 参数）
        .orElse(providers.environmentVariable(key)) // 2. 环境变量
        .orElse(providers.gradleProperty(key))  // 3. gradle.properties
        .getOrNull()
}
