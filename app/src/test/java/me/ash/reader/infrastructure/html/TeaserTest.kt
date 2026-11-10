package me.ash.reader.infrastructure.html

import com.rometools.rome.io.SyndFeedInput
import java.io.InputStreamReader
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TeaserTest {

    private fun description(fixture: String): String {
        val stream = checkNotNull(javaClass.getResourceAsStream("/fixtures/$fixture"))
        val feed = SyndFeedInput().build(InputStreamReader(stream, Charsets.UTF_8))
        return feed.entries.single().description.value
    }

    @Test
    fun bbcItemIsATeaser() {
        assertTrue(description("bbc-item.xml").isTeaser())
    }

    @Test
    fun emptyDescriptionIsATeaser() {
        assertTrue("".isTeaser())
    }

    @Test
    fun g1ItemWithBodyIsNotATeaser() {
        assertFalse(description("g1-item.xml").isTeaser())
    }

    @Test
    fun longSingleParagraphIsNotATeaser() {
        assertFalse("<p>${"word ".repeat(120)}</p>".isTeaser())
    }
}
