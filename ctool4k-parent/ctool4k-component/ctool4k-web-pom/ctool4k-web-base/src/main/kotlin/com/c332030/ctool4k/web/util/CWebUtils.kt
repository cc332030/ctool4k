package com.c332030.ctool4k.web.util

import com.c332030.ctool4k.core.util.ignoreCaseSetOf
import java.io.InputStream
import java.io.OutputStream
import java.util.*

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

val forwardHeadersIgnore: Set<String> = ignoreCaseSetOf(
    "host"
)

fun forward(
    inputStream: InputStream,
    outputStream: OutputStream,
    headerNameEnumeration: Enumeration<String>,
    getHeaderFunc:  (String) -> String?,
    setHeaderFunc: (String, String) -> Unit,
) {
    forward(
        inputStream,
        outputStream,
        headerNameEnumeration.asIterator(),
        getHeaderFunc,
        setHeaderFunc,
    )
}

fun forward(
    inputStream: InputStream,
    outputStream: OutputStream,
    headerNameIter: Iterator<String>,
    getHeaderFunc:  (String) -> String?,
    setHeaderFunc: (String, String) -> Unit,
) {

    while (headerNameIter.hasNext()) {

        val headerName = headerNameIter.next()
        if(forwardHeadersIgnore.contains(headerName)) {
            continue
        }

        val headerValue = getHeaderFunc(headerName)
        if(!headerValue.isNullOrEmpty()) {
            setHeaderFunc.invoke(headerName, headerValue)
        }
    }

    inputStream.transferTo(outputStream)

}
