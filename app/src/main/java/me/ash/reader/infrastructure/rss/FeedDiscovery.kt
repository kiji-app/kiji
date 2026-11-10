package me.ash.reader.infrastructure.rss

import java.net.URI
import java.util.Locale
import org.jsoup.Jsoup

private val FEED_MIME_TYPES =
    setOf("application/rss+xml", "application/atom+xml", "application/rdf+xml")

private val COMMON_FEED_PATHS =
    listOf("/feed", "/rss", "/rss.xml", "/feed.xml", "/atom.xml", "/index.xml")

fun candidateFeedLinks(pageUrl: String, html: String): List<String> {
    val alternates =
        Jsoup.parse(html, pageUrl).select("head link[rel~=(?i)alternate][href]").map {
            it.attr("type").lowercase(Locale.ROOT) to it.absUrl("href")
        }
    val declared =
        alternates.filter { it.first in FEED_MIME_TYPES }.map { it.second } +
            alternates.filter { it.first.isBlank() }.map { it.second }
    return (declared + commonFeedLinks(pageUrl)).filter { it.isNotBlank() }.distinct()
}

private fun commonFeedLinks(pageUrl: String): List<String> {
    val uri = runCatching { URI(pageUrl) }.getOrNull() ?: return emptyList()
    val host = uri.host ?: return emptyList()
    val port = if (uri.port == -1) "" else ":${uri.port}"
    return COMMON_FEED_PATHS.map { "${uri.scheme}://$host$port$it" }
}
