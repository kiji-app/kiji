package me.ash.reader.infrastructure.html

import org.jsoup.Jsoup

private const val TEASER_MAX_CHARS = 400

fun String.isTeaser(): Boolean {
    val body = Jsoup.parseBodyFragment(this).body()
    return body.text().length < TEASER_MAX_CHARS && body.select("p").size <= 1 &&
        body.wholeText().lines().count { it.isNotBlank() } <= 1
}
