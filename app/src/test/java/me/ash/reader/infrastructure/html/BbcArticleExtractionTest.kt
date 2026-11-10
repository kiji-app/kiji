package me.ash.reader.infrastructure.html

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BbcArticleExtractionTest {

    private val url = "https://www.bbc.com/portuguese/articles/c6y43492vrzxo"

    private fun extract() =
        checkNotNull(
            Readability.parseToElement(
                checkNotNull(javaClass.getResourceAsStream("/fixtures/bbc-article.html"))
                    .readBytes()
                    .toString(Charsets.UTF_8),
                url,
            )
        )

    @Test
    fun extractsTheArticleBody() {
        assertTrue(extract().select("p").size >= 10)
    }

    @Test
    fun keepsImagesInPlace() {
        assertTrue(extract().select("img").isNotEmpty())
    }

    @Test
    fun dropsMenuAndFooter() {
        val text = extract().text()
        assertFalse(text.contains("Menu BBC Navegação"))
        assertFalse(text.contains("Rodapé BBC"))
    }
}
