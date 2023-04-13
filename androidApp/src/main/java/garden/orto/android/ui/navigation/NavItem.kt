package garden.orto.android.ui.navigation

import androidx.navigation.navArgument

sealed class NavItem(
    private val baseRoute: String,
    private val navArgs: List<NavArg<*>> = emptyList()
) {
    private fun formatQueryString(formatStrings: Collection<String>): String {
        require(navArgs.size == formatStrings.size)
        return "?" + navArgs.zip(formatStrings).joinToString("&") { "${it.first.key}=${it.second}" }
    }
    private fun formatArgKeys() = formatQueryString(navArgs.map { "{${it.key}}" })
    protected fun formatNavRoute(args: Map<String, String>) =
        baseRoute + formatQueryString(
            navArgs.map { args[it.key]!! }
        )

    val route = run {
        baseRoute + formatArgKeys()
    }

    val args = navArgs.map {
        navArgument(it.key) {
            type = it.navType
            defaultValue = it.defaultValue
            nullable = it.defaultValue == null
        }
    }

    open class TagDetailNavItem(defaultTagName: String) : NavItem(
        "tagDetail",
        listOf(
            NavArg.TagName(defaultTagName)
        )
    ) {
        fun createNavRoute(tagName: String) = formatNavRoute(
            mapOf(
                args[0].name to tagName
            )
        )
    }

    object NoteCreateNavItem : NavItem("noteCreate")
}