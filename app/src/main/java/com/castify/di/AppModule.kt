package com.castify.di

import com.castify.core.networking.HttpClientFactory
import com.castify.core.utils.AssetsReader
import com.castify.domain.repositories.MovieRepository
import com.castify.domain.repositoriesImpl.MovieRepositoryImpl
import com.castify.presentation.screens.home.HomeViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    viewModelOf(::HomeViewModel)

    single { AssetsReader(androidContext()) }

}