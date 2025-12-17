package com.ethan.easy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val title: String = "New Chat",
    val createdAt: Long,
    val modelProvider: String
)
