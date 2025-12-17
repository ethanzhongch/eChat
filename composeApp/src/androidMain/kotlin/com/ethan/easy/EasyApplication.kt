package com.ethan.easy

import android.app.Application
import com.ethan.easy.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EasyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EasyApplication)
            modules(appModule)
        }
    }
}
