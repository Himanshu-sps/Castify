package com.castify.core.networking

import com.castify.core.utils.NetworkError
import com.castify.core.utils.ResultWrapper
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): ResultWrapper<T, NetworkError> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return ResultWrapper.Error(NetworkError.NO_INTERNET)
    } catch(e: SerializationException) {
        return ResultWrapper.Error(NetworkError.SERIALIZATION)
    } catch(e: Exception) {
        coroutineContext.ensureActive()
        return ResultWrapper.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}