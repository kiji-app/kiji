package me.ash.reader.infrastructure.rss

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedDiscoveryTest {

    private val page = "https://example.com/blog/post"

    @Test
    fun declaredFeedComesFirstAndIsResolvedAgainstThePage() {
        val html =
            """<html><head>
            <link rel="alternate" type="application/rss+xml" href="/blog/rss.xml">
            </head></html>"""

        assertEquals("https://example.com/blog/rss.xml", candidateFeedLinks(page, html).first())
    }

    @Test
    fun typedFeedsComeBeforeUntypedAlternates() {
        val html =
            """<html><head>
            <link rel="alternate" href="/untyped">
            <link rel="alternate" type="application/atom+xml" href="/atom">
            </head></html>"""

        val links = candidateFeedLinks(page, html)

        assertEquals("https://example.com/atom", links[0])
        assertEquals("https://example.com/untyped", links[1])
    }

    @Test
    fun otherAlternatesAreIgnored() {
        val html =
            """<html><head>
            <link rel="alternate" type="text/html" hreflang="en" href="/en">
            <link rel="alternate" type="application/json+oembed" href="/oembed">
            </head></html>"""

        val links = candidateFeedLinks(page, html)

        assertFalse(links.contains("https://example.com/en"))
        assertFalse(links.contains("https://example.com/oembed"))
    }

    @Test
    fun pageWithoutLinksFallsBackToCommonPathsOnTheSiteRoot() {
        val links = candidateFeedLinks("https://example.com:8080/a/b", "<html></html>")

        assertTrue(links.contains("https://example.com:8080/feed"))
        assertTrue(links.contains("https://example.com:8080/rss.xml"))
        assertTrue(links.contains("https://example.com:8080/atom.xml"))
    }

    @Test
    fun duplicatesAreRemoved() {
        val html =
            """<html><head>
            <link rel="alternate" type="application/rss+xml" href="/feed">
            </head></html>"""

        val links = candidateFeedLinks(page, html)

        assertEquals(1, links.count { it == "https://example.com/feed" })
    }
}
