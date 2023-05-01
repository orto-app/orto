package garden.orto.android.ui.navigation

import androidx.navigation.NavType

sealed class NavArg<T : Any?>(val key: String, val navType: NavType<T>, val defaultValue: T) {
    data class TagName(val defaultTagName: String) : NavArg<String?>("tagName", NavType.StringType, defaultTagName)
}