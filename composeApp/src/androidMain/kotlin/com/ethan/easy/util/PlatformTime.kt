package com.ethan.easy.util

actual object PlatformTime {
    actual fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
