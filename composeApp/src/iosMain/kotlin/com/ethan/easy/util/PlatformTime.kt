package com.ethan.easy.util

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object PlatformTime {
    actual fun getCurrentTimeMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }
}
