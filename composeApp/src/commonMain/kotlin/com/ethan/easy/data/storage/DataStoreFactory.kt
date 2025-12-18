package com.ethan.easy.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

expect fun createDataStore(producePath: () -> String): DataStore<Preferences>

internal const val DATASTORE_FILE_NAME = "user_settings.preferences_pb"
