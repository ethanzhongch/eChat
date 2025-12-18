package com.ethan.easy.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ethan.easy.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.ethan.easy.data.storage.createDataStore
import com.ethan.easy.data.storage.DATASTORE_FILE_NAME

actual val platformModule = module {
    single<AppDatabase> {
        val context = androidContext()
        val dbFile = context.getDatabasePath("echat.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
    }

    single { createDataStore { androidContext().filesDir.resolve(DATASTORE_FILE_NAME).absolutePath } }
}
