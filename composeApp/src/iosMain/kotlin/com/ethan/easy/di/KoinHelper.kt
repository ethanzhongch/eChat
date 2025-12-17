package com.ethan.easy.di

import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(appModule)
    }
}
