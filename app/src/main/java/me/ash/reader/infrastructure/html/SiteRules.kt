package me.ash.reader.infrastructure.html

import java.net.URI
import org.jsoup.Jsoup

private val STRIP_SELECTORS_BY_HOST =
    mapOf(
        "bbc.com" to listOf("[data-testid=byline]", "#save-article-button"),
        "bbc.co.uk" to listOf("[data-testid=byline]", "#save-article-button"),
    )

fun applySiteRules(html: String, uri: String?): String {
    val host = runCatching { URI(uri ?: return html).host }.getOrNull() ?: return html
    val selectors =
        STRIP_SELECTORS_BY_HOST.entries
            .firstOrNull { host == it.key || host.endsWith(".${it.key}") }
            ?.value ?: return html
    val doc = Jsoup.parse(html)
    selectors.forEach { doc.select(it).remove() }
    return doc.outerHtml()
}
