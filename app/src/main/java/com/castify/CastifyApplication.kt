package com.castify

import android.app.Application
import com.castify.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CastifyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CastifyApplication)
            androidLogger()

            modules(appModule)
        }
    }
}