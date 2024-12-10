package com.castify.core.utils

typealias DomainError = Error

sealed interface ResultWrapper<out D, out E: Error> {
    data class Success<out D>(val data: D): ResultWrapper<D, Nothing>
    data class Error<out E: DomainError>(val error: E): ResultWrapper<Nothing, E>
}

inline fun <T, E: Error, R> ResultWrapper<T, E>.map(map: (T) -> R): ResultWrapper<R, E> {
    return when(this) {
        is ResultWrapper.Error -> ResultWrapper.Error(error)
        is ResultWrapper.Success -> ResultWrapper.Success(map(data))
    }
}

fun <T, E: Error> ResultWrapper<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Error> ResultWrapper<T, E>.onSuccess(action: (T) -> Unit): ResultWrapper<T, E> {
    return when(this) {
        is ResultWrapper.Error -> this
        is ResultWrapper.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: Error> ResultWrapper<T, E>.onError(action: (E) -> Unit): ResultWrapper<T, E> {
    return when(this) {
        is ResultWrapper.Error -> {
            action(error)
            this
        }
        is ResultWrapper.Success -> this
    }
}

typealias EmptyResult<E> = ResultWrapper<Unit, E>