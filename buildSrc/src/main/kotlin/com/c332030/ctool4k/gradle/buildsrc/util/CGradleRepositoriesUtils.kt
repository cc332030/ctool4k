package com.c332030.ctool4k.gradle.buildsrc.util

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

/**
 * <p>
 *   Description: CGradleRepositoriesUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/3
 */

fun RepositoryHandler.configureSharedRepositories(project: Project) {

    // 从配置中读取仓库相关参数
    val mavenCentralUrl = project.getConfigValue("MAVEN_CENTRAL")
    val nexusUsername = project.getConfigValue("NEXUS_USERNAME")
    val nexusPassword = project.getConfigValue("NEXUS_PASSWORD")
    val nexusSnapshotUrl = project.getConfigValue("NEXUS_SNAPSHOT_URL")
    val nexusReleaseUrl = project.getConfigValue("NEXUS_RELEASE_URL")

    // 核心仓库配置（与原逻辑完全一致）
    mavenLocal()

    if (!mavenCentralUrl.isNullOrEmpty()) {
        maven {
            url = project.uri(mavenCentralUrl)
        }
    }

    if (!nexusSnapshotUrl.isNullOrEmpty()) {
        maven {
            url = project.uri(nexusSnapshotUrl)
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }

    if (!nexusReleaseUrl.isNullOrEmpty()) {
        maven {
            url = project.uri(nexusReleaseUrl)
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }

    mavenCentral()

}
