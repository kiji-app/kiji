package me.ash.reader.infrastructure.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidDatabaseMigrationTest {

    private val dbName = "migration-test"

    @get:Rule
    val helper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AndroidDatabase::class.java,
        )

    @Test
    fun migrate5To8KeepsData() = migrateAndCheck(from = 5)

    @Test
    fun migrate6To8KeepsData() = migrateAndCheck(from = 6)

    @Test
    fun migrate7To8KeepsData() = migrateAndCheck(from = 7)

    private fun migrateAndCheck(from: Int) {
        helper.createDatabase(dbName, from).use { populate(it) }

        helper.runMigrationsAndValidate(dbName, 8, true, MIGRATION_7_8).use { db ->
            assertEquals(ARTICLES, count(db, "SELECT COUNT(*) FROM article"))
            assertEquals(1, count(db, "SELECT COUNT(*) FROM feed"))
            assertEquals(1, count(db, "SELECT COUNT(*) FROM account"))
            assertEquals(1, count(db, "SELECT COUNT(*) FROM `group`"))
            assertEquals(
                1,
                count(db, "SELECT COUNT(*) FROM article WHERE id = 'a1' AND title = 'Title 1' AND isStarred = 1"),
            )
            assertEquals(
                6,
                count(db, "SELECT COUNT(*) FROM sqlite_master WHERE type = 'index' AND name LIKE 'index_article_accountId_%'"),
            )
        }
    }

    private fun populate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "INSERT INTO account (id, name, type, syncInterval, syncOnStart, syncOnlyOnWiFi, syncOnlyWhenCharging, keepArchived, syncBlockList) " +
                "VALUES (1, 'Local', 1, 1, 0, 0, 0, 0, '')"
        )
        db.execSQL("INSERT INTO `group` (id, name, accountId) VALUES ('g1', 'News', 1)")
        db.execSQL(
            "INSERT INTO feed (id, name, url, groupId, accountId, isNotification, isFullContent) " +
                "VALUES ('f1', 'Feed', 'https://example.com/feed', 'g1', 1, 0, 0)"
        )
        for (i in 1..ARTICLES) {
            db.execSQL(
                "INSERT INTO article (id, date, title, rawDescription, shortDescription, link, feedId, accountId, isUnread, isStarred, isReadLater) " +
                    "VALUES ('a$i', ${1_700_000_000_000L + i}, 'Title $i', '<p>body $i</p>', 'body $i', 'https://example.com/$i', 'f1', 1, ${i % 2}, ${if (i == 1) 1 else 0}, 0)"
            )
        }
    }

    private fun count(db: SupportSQLiteDatabase, sql: String): Int =
        db.query(sql).use {
            it.moveToFirst()
            it.getInt(0)
        }

    private companion object {
        const val ARTICLES = 200
    }
}
