package com.gurpreetsk.tvdb

import com.gurpreetsk.tvdb.data.ErrorType
import com.gurpreetsk.tvdb.data.HttpException
import com.gurpreetsk.tvdb.data.toApplicationError
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ApplicationErrorTests {
    @Test
    fun `should specify network error type for IO exceptions`() {
        val ioException = IOException("Socket timeout exception")

        val applicationError = ioException.toApplicationError()

        assertTrue { applicationError.type == ErrorType.NETWORK }
        assertSame(ioException.message, applicationError.message)
    }

    @Test
    fun `should specify network error type for HTTP exceptions`() {
        val httpException =
            HttpException(404, "HTTP 404")

        val applicationError = httpException.toApplicationError()

        assertTrue { applicationError.type == ErrorType.NETWORK }
        assertSame(httpException.message, applicationError.message)
    }

    @Test
    fun `should return unknown error type for unknown exceptions`() {
        val unknownException = UnknownException()

        val applicationError = unknownException.toApplicationError()

        assertTrue { applicationError.type == ErrorType.UNKNOWN }
        assertNull(unknownException.message)
    }

    @Test
    fun `should return unknown error type for runtime exceptions`() {
        val httpException = RuntimeException()

        val applicationError = httpException.toApplicationError()

        assertTrue { applicationError.type == ErrorType.UNKNOWN }
        assertNull(httpException.message)
    }
}

private class UnknownException : Exception()
