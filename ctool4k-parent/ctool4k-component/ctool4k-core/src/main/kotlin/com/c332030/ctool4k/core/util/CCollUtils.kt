package com.c332030.ctool4k.core.util

import java.util.*

/**
 * <p>
 *   Description: CCollUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */

fun ignoreCaseSetOf(vararg elements: String): TreeSet<String> {
    return sortedSetOf(String.CASE_INSENSITIVE_ORDER, *elements)
}
