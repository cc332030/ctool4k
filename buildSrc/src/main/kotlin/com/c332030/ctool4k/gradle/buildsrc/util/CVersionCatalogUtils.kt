package com.c332030.ctool4k.gradle.buildsrc.util

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

/**
 * <p>
 *   Description: VersionCatalogUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/3
 */

fun VersionCatalog.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(alias)
        .orElseThrow<IllegalStateException> {
            throw IllegalStateException("library alias: $alias is not defined!")
        }
}

fun VersionCatalog.getBundle(alias: String): Provider<ExternalModuleDependencyBundle> {
    return findBundle(alias)
        .orElseThrow<IllegalStateException> {
            throw IllegalStateException("bundle alias: $alias is not defined!")
        }
}

fun VersionCatalog.getVersion(alias: String): VersionConstraint {
    return findVersion(alias)
        .orElseThrow<IllegalStateException> {
            throw IllegalStateException("version alias: $alias is not defined!")
        }
}

fun VersionCatalog.getPlugin(alias: String): Provider<PluginDependency> {
    return findPlugin(alias)
        .orElseThrow<IllegalStateException> {
            throw IllegalStateException("plugin alias: $alias is not defined!")
        }
}
