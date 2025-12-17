package com.ethan.easy.di

import com.ethan.easy.data.database.AppDatabase
import com.ethan.easy.data.repository.ChatRepository
import com.ethan.easy.ui.chat.ChatViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    includes(platformModule)

    single { get<AppDatabase>().sessionDao() }
    single { get<AppDatabase>().messageDao() }
    
    singleOf(::ChatRepository)
    factoryOf(::ChatViewModel)
}
