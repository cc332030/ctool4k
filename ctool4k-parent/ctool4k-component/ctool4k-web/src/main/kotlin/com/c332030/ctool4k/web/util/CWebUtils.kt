package com.c332030.ctool4k.web.util

import java.io.InputStream
import java.io.OutputStream

/**
 * <p>
 *   Description: CWebUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */
fun forward(
    inputStream: InputStream,
    outputStream: OutputStream,
    headers: Map<String, Collection<Object>> = mapOf(),
    setHeaderFunc: (String, Collection<Object>) -> Unit,
) {

    headers.forEach(setHeaderFunc)
    inputStream.transferTo(outputStream)

}
