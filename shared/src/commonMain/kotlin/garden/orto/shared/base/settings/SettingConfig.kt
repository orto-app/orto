package garden.orto.shared.base.settings

import com.russhwolf.settings.Settings

/**
 * This class wraps all of the different operations that might be performed on a given [key], and adds an interface to
 * get and set it as a [String] value.
 */
sealed class SettingConfig<T>(
    private val settings: Settings,
    val key: String,
    private val defaultValue: T
) {
    protected abstract fun doGetValue(settings: Settings, key: String, defaultValue: T): T
    protected abstract fun doSetValue(settings: Settings, key: String, value: T)

    fun remove() = settings.remove(key)
    fun exists(): Boolean = settings.hasKey(key)

    fun get(): T = doGetValue(settings, key, defaultValue)
    fun set(value: T): Boolean {
        return try {
            doSetValue(settings, key, value)
            true
        } catch (exception: Exception) {
            false
        }
    }

    override fun toString() = key
}


sealed class NullableSettingConfig<T : Any>(
    settings: Settings,
    key: String
) : SettingConfig<T?>(settings, key, null) {
    constructor(
        settings: Settings,
        key: String,
        defaultValue: T
    ) : this(settings, key) {
        set(defaultValue)
    }

    protected abstract fun doGetValue(settings: Settings, key: String): T

    final override fun doGetValue(settings: Settings, key: String, defaultValue: T?): T =
        doGetValue(settings, key)
}