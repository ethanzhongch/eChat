package com.ethan.easy.di

import com.ethan.easy.data.database.AppDatabase
import com.ethan.easy.data.repository.ChatRepository
import com.ethan.easy.ui.chat.ChatViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

import com.ethan.easy.api.LLMFactory
import com.ethan.easy.data.repository.SettingsRepository
import com.ethan.easy.ui.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val appModule = module {
    includes(platformModule)

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }

    singleOf(::SettingsRepository)
    single { LLMFactory(get(), get()) }

    single { get<AppDatabase>().sessionDao() }
    single { get<AppDatabase>().messageDao() }
    
    single { ChatRepository(get(), get(), get()) }
    factory { ChatViewModel(get(), get()) }
    factory { SettingsViewModel(get()) }
}
