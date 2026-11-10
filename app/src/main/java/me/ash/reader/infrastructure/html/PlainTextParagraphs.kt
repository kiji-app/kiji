package me.ash.reader.infrastructure.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

private const val BLOCK_TAGS =
    "p, div, ul, ol, h1, h2, h3, h4, h5, h6, blockquote, pre, table, figure"

// Some feeds (g1) send the article as plain text with one paragraph per line and no block tags,
// and the reader collapses newlines into spaces.
fun String.splitPlainTextParagraphs(): String {
    val body = Jsoup.parseBodyFragment(this).body()
    if (body.select(BLOCK_TAGS).isNotEmpty()) return this

    val multiLineNodes = body.childNodes().filterIsInstance<TextNode>().filter { '\n' in it.wholeText }
    if (multiLineNodes.isEmpty()) return this

    multiLineNodes.forEach { node ->
        node.wholeText
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { line -> node.before(Element("p").text(line)) }
        node.remove()
    }
    body.ownerDocument()?.outputSettings()?.prettyPrint(false)
    return body.html()
}
