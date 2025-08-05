package pnemonic.clock_always_on

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform