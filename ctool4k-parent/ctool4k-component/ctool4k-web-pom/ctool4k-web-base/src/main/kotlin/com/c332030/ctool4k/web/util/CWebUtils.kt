package com.c332030.ctool4k.web.util

import java.io.InputStream
import java.io.OutputStream
import java.util.Enumeration

/**
 * <p>
 *   Description: CWebUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */
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
    headerNameEnumeration: Enumeration<String>,
    getHeaderFunc:  (String) -> String?,
    setHeaderFunc: (String, String) -> Unit,
) {

    while (headerNameEnumeration.hasMoreElements()) {

        val headerName = headerNameEnumeration.nextElement()
        val headerValue = getHeaderFunc(headerName)
        if(!headerValue.isNullOrEmpty()) {
            setHeaderFunc.invoke(headerName, headerValue)
        }
    }

    inputStream.transferTo(outputStream)

}
