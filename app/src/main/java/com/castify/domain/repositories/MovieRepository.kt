package com.castify.domain.repositories

import com.castify.core.utils.NetworkError
import com.castify.core.utils.ResultWrapper
import com.castify.data.dto.HomePageResponse

interface MovieRepository {
    suspend fun getHomePageData(): ResultWrapper<List<HomePageResponse>, NetworkError>
    suspend fun getHomePageDataFromAssets(): List<HomePageResponse>

}