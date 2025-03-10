package com.castify.core.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

object HttpClientFactory {

    fun create(engine: HttpClientEngine): HttpClient {

        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            // use gson content negotiation for serialize or deserialize
            install(ContentNegotiation) {
                gson()
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}