package me.ash.reader.infrastructure.html

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IclArticleExtractionTest {

    private fun extract() =
        checkNotNull(
            Readability.parseToElement(
                checkNotNull(javaClass.getResourceAsStream("/fixtures/icl-article.html"))
                    .readBytes()
                    .toString(Charsets.UTF_8),
                "https://iclnoticias.com.br/economia/dinheiro-esquecido-sistema-bilhoes/",
            )
        )

    @Test
    fun extractsTheFullArticleNotJustTheExcerpt() {
        val element = extract()

        assertTrue(element.select("p").size >= 10)
        assertTrue(element.html().hasArticleBody())
    }

    @Test
    fun dropsTheAudioPlayer() {
        assertFalse(extract().text().contains("00:00"))
    }
}
