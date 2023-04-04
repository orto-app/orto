package garden.orto.shared.cache.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import garden.orto.shared.cache.OrtoDatabase

actual fun createInMemorySqlDriver(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    OrtoDatabase.Schema.create(driver)
    return driver
}