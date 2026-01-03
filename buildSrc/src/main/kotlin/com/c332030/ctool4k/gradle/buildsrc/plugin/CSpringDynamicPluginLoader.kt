package com.c332030.ctool4k.gradle.buildsrc.plugin

import com.c332030.ctool4k.gradle.buildsrc.constant.JDK_VERSION
import com.c332030.ctool4k.gradle.buildsrc.plugin.SpringPlugins.SPRING_BOOT2
import com.c332030.ctool4k.gradle.buildsrc.plugin.SpringPlugins.SPRING_BOOT4
import com.c332030.ctool4k.gradle.buildsrc.util.getConfigValue
import com.c332030.ctool4k.gradle.buildsrc.util.getPlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependenciesSpec

/**
 * <p>
 *   Description: CSpringDynamicPluginLoader
 * </p>
 *
 * @author c332030
 * @since 2026/1/3
 */

object SpringPlugins {
    const val SPRING_BOOT = "spring-boot"
    const val SPRING_BOOT2 = "${SPRING_BOOT}2"
    const val SPRING_BOOT4 = "${SPRING_BOOT}4"
}

fun PluginDependenciesSpec.applySpringPlugins(project: Project) {

    val jdkVersion = project.getConfigValue(JDK_VERSION)

    val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
    if("8" == jdkVersion) {
        val springBoot2 = libs.getPlugin(SPRING_BOOT2)
        alias(springBoot2)
    } else {
        val springBoot4 = libs.getPlugin(SPRING_BOOT4)
        alias(springBoot4)
    }

}
