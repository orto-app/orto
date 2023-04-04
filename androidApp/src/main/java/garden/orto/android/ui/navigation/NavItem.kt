package garden.orto.android.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    internal val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argKeys)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    object TagDetail : NavItem(
        "tagDetail",
        listOf(
            NavArg.NameTag
        )
    ) {
        fun createNavRoute(tagName: String) = "$baseRoute/$tagName"
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    NameTag("nameTag", NavType.StringType)
}