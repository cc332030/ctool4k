package com.c332030.ctool4k.core.util

import java.util.Enumeration

/**
 * <p>
 *   Description: CIterUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */
fun <T> toIterator(iter: Enumeration<T>): Iterator<T> {
    return object : Iterator<T> {
        override fun hasNext(): Boolean {
            return iter.hasMoreElements()
        }

        override fun next(): T {
            return iter.nextElement()
        }
    }
}
