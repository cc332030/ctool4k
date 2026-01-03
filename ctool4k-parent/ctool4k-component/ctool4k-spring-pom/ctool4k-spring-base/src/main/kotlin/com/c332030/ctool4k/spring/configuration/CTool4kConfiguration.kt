package com.c332030.ctool4k.spring.configuration

import com.c332030.ctool4k.definition.constant.BASE_PACKAGE
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * <p>
 *   Description: CTool4kConfiguration
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */
@Configuration
@ComponentScan(BASE_PACKAGE)
@ConfigurationPropertiesScan(BASE_PACKAGE)
class CTool4kConfiguration
