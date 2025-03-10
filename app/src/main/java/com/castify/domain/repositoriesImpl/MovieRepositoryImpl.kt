package com.castify.domain.repositoriesImpl

import com.castify.core.networking.safeCall
import com.castify.core.utils.AssetsReader
import com.castify.core.utils.NetworkError
import com.castify.core.utils.ResultWrapper
import com.castify.core.utils.map
import com.castify.data.dto.HomePageResponse
import com.castify.domain.CachedDataReader
import com.castify.domain.getHomePageList
import com.castify.domain.repositories.MovieRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class MovieRepositoryImpl(
    private val httpClient: HttpClient,
    private val assetsReader: AssetsReader
): MovieRepository {

    private val top250MovieDataReader = CachedDataReader {
        getHomePageList(assetsReader, "movies.json")
    }

    override suspend fun getHomePageData(): ResultWrapper<List<HomePageResponse>, NetworkError> {
        return safeCall<List<HomePageResponse>> {
            httpClient.get(
                urlString = /*constructUrl("/assets")*/""
            )
        }.map { response ->
            response
        }
    }

    override suspend fun getHomePageDataFromAssets(): List<HomePageResponse> {
        return top250MovieDataReader.read()
    }

}