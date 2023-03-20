package coop.uwutech.orto.shared.cache.local

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import coop.uwutech.orto.shared.cache.OrtoDatabase

private var index = 0

actual fun createInMemorySqlDriver(): SqlDriver {
    index++
    val schema = OrtoDatabase.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "orto-$index.db",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) {
                    schema.migrate(it, oldVersion, newVersion)
                }
            },
            inMemory = true
        )
    )
}