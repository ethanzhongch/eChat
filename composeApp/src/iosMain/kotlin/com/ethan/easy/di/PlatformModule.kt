package com.ethan.easy.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ethan.easy.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

import kotlinx.cinterop.ExperimentalForeignApi
import com.ethan.easy.data.storage.createDataStore
import com.ethan.easy.data.storage.DATASTORE_FILE_NAME

actual val platformModule = module {
    single<AppDatabase> {
        val dbFilePath = documentDirectory() + "/echat.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
    }

    single { createDataStore { documentDirectory() + "/$DATASTORE_FILE_NAME" } }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
