package me.ash.reader.infrastructure.html

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleBodyTest {

    private fun extract(fixture: String, url: String): String =
        checkNotNull(
                Readability.parseToElement(
                    checkNotNull(javaClass.getResourceAsStream("/fixtures/$fixture"))
                        .readBytes()
                        .toString(Charsets.UTF_8),
                    url,
                )
            )
            .html()

    @Test
    fun g1VideoPageHasNoArticleBody() {
        val html =
            extract(
                "g1-video-page.html",
                "https://g1.globo.com/pa/para/edicao/2026/11/10/videos-jl1-de-terca-feira-10-de-novembro-de-2026.ghtml",
            )
        assertFalse(html.hasArticleBody())
    }

    @Test
    fun bbcArticleHasArticleBody() {
        val html =
            extract("bbc-article.html", "https://www.bbc.com/portuguese/articles/c6y43492vrzxo")
        assertTrue(html.hasArticleBody())
    }
}
