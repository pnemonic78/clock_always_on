package pnemonic.clock_always_on

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
    val is24Hours: Boolean
}

//expect fun getPlatform(): Platform

@Composable
expect fun rememberPlatform(): Platform
