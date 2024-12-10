package com.castify.core.networking

import com.castify.core.utils.NetworkError
import com.castify.core.utils.ResultWrapper
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ResultWrapper<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                ResultWrapper.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                ResultWrapper.Error(NetworkError.SERIALIZATION)
            }
        }
        408 -> ResultWrapper.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> ResultWrapper.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> ResultWrapper.Error(NetworkError.SERVER_ERROR)
        else -> ResultWrapper.Error(NetworkError.UNKNOWN)
    }
}