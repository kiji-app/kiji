package me.ash.reader.infrastructure.html

import com.rometools.rome.io.SyndFeedInput
import java.io.InputStreamReader
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlainTextParagraphsTest {

    private fun g1Description(): String {
        val stream = checkNotNull(javaClass.getResourceAsStream("/fixtures/g1-item.xml"))
        val feed = SyndFeedInput().build(InputStreamReader(stream, Charsets.UTF_8))
        return feed.entries.single().description.value
    }

    @Test
    fun g1ItemBecomesOneParagraphPerLine() {
        val body = Jsoup.parseBodyFragment(g1Description().splitPlainTextParagraphs()).body()
        val paragraphs = body.select("p").map { it.text() }

        assertEquals(6, paragraphs.size)
        assertTrue(paragraphs[0].startsWith("Goiânia registrou volumes"))
        assertTrue(paragraphs[3].startsWith("De acordo com o informativo"))
        assertTrue(paragraphs.last().startsWith("O Cimehgo alerta"))
    }

    @Test
    fun g1ItemKeepsLeadingImageOutsideParagraphs() {
        val body = Jsoup.parseBodyFragment(g1Description().splitPlainTextParagraphs()).body()

        assertEquals(1, body.select("img").size)
        assertTrue(body.select("p img").isEmpty())
    }

    @Test
    fun g1ItemWithoutTheFixHasNoParagraphs() {
        assertTrue(Jsoup.parseBodyFragment(g1Description()).body().select("p").isEmpty())
    }

    @Test
    fun htmlWithBlockTagsIsReturnedUntouched() {
        val html = "<p>first</p>\n<p>second</p>"

        assertEquals(html, html.splitPlainTextParagraphs())
    }

    @Test
    fun textWithoutNewlinesIsReturnedUntouched() {
        val text = "A single line with <b>bold</b> text"

        assertEquals(text, text.splitPlainTextParagraphs())
    }

    @Test
    fun blankLinesAndSurroundingSpacesAreDropped() {
        val result = "  one  \n\n   \n two\n".splitPlainTextParagraphs()

        assertEquals("<p>one</p><p>two</p>", result)
    }

    @Test
    fun specialCharactersAreEscaped() {
        val result = "a < b & c\nsecond".splitPlainTextParagraphs()

        assertEquals("<p>a &lt; b &amp; c</p><p>second</p>", result)
    }
}
