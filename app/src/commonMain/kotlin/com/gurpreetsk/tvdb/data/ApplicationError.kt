package com.gurpreetsk.tvdb.data

import com.gurpreetsk.tvdb.shared.Restorable
import com.gurpreetsk.tvdb.shared.SaveState
import kotlinx.io.IOException

@SaveState
data class ApplicationError(
    val type: ErrorType,
    val message: String? = null
) : Restorable

class HttpException(
    val code: Int,
    val reason: String
) : IOException(reason)

fun Throwable.toApplicationError(): ApplicationError {
    return when (this) {
        is HttpException,
        is IOException -> ApplicationError(
            ErrorType.NETWORK,
            this.message
        )

        // TODO(gs) Decide use case for SYSTEM errors

        else -> ApplicationError(ErrorType.UNKNOWN)
    }
}
