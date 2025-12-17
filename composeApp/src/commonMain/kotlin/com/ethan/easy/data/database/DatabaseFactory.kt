package com.ethan.easy.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): AppDatabase
}
