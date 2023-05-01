package garden.orto.shared.base.settings

import com.russhwolf.settings.Settings

class StringSettingConfig(settings: Settings, key: String, defaultValue: String) :
    SettingConfig<String>(settings, key, defaultValue) {

    override fun doGetValue(settings: Settings, key: String, defaultValue: String): String =
        settings.getString(key, defaultValue)

    override fun doSetValue(settings: Settings, key: String, value: String) =
        settings.putString(key, value)
}

class NullableStringSettingConfig(settings: Settings, key: String, defaultValue: String) :
    NullableSettingConfig<String>(
        settings,
        key,
        defaultValue
    ) {

    override fun doSetValue(settings: Settings, key: String, value: String?) {
        if (value != null) {
            settings.putString(key, value)
        } else {
            settings.remove(key)
        }
    }


    override fun doGetValue(settings: Settings, key: String): String =
        settings.getStringOrNull(key).toString()
}