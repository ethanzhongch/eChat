package com.ethan.easy.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): AppDatabase {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("echat.db")
        return Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
        .setDriver(BundledSQLiteDriver())
        .build()
    }
}
