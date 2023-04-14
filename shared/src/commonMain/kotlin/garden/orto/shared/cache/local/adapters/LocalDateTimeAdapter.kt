package garden.orto.shared.cache.local.adapters

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.*

val localDateTimeAdapter = object : ColumnAdapter<LocalDateTime, Long> {
    override fun decode(databaseValue: Long) =
        Instant.fromEpochSeconds(databaseValue).toLocalDateTime(
            TimeZone.currentSystemDefault()
        )

    override fun encode(value: LocalDateTime) =
        value.toInstant(TimeZone.currentSystemDefault()).epochSeconds
}