package com.ethan.easy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform