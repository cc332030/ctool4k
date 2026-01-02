package com.c332030.ctool4k.web.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * <p>
 *   Description: CWebExUtils
 * </p>
 *
 * @author c332030
 * @since 2026/1/2
 */
fun forward(
    request: HttpServletRequest,
    response: HttpServletResponse
) {

    forward(
        request.inputStream,
        response.outputStream,
        request.headerNames,
        request::getHeader,
        response::setHeader
    )

}
