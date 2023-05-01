package garden.orto.shared.repository

import com.russhwolf.settings.Settings
import garden.orto.shared.base.settings.SettingConfig
import garden.orto.shared.base.settings.StringSettingConfig
import garden.orto.shared.domain.ISettingsRepository

class SettingRepositoryImp(private val settings: Settings) : ISettingsRepository {
    override val settingConfigs: Set<SettingConfig<*>> = setOf(
        StringSettingConfig(settings,"ui.home", "home")
    )

    init {
        val count = mutableMapOf<String, Int>()
        for (s in settingConfigs.map { it.key }) {
            count[s] = count.getOrElse(s) { 0 } + 1
        }
        // Check that each key is unique
        check(count.values.toSet() == setOf(1))
    }

    override fun <T> getByKey(key: String): SettingConfig<T> {
        for (s in settingConfigs) {
            if (s.key == key ) {
                return s as SettingConfig<T>
            }
        }
        throw IllegalStateException("No setting found for key $key")
    }

    override fun clear() = settings.clear()
}