package garden.orto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform