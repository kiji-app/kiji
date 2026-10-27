<div align="center">
    <img width="160" height="160" src="fastlane/metadata/android/en-US/images/icon.png" alt="Kiji icon" />
    <h1>Kiji</h1>
    <p>An Android RSS reader and news aggregator focused on clean, readable articles.</p>
</div>

## About

Kiji (記事, "article") is an Android RSS reader written in Kotlin. The goal is reading quality: article text delivered clean, with paragraphs and images in place.

## Based on Read You

Kiji is a derivative work of [Read You](https://github.com/ReadYouApp/ReadYou) by Ash and the Read You contributors, and keeps its full commit history. Many thanks to the original authors.

The code was modified from Read You: the app was renamed and rebranded, links to the original project were replaced, and further UI, UX and feature changes are in progress. The upstream repository is tracked as the `upstream` remote so that fixes can be compared and brought over.

## Building

```bash
./gradlew assembleGithubDebug
```

Requires JDK 17+ and the Android SDK (`sdk.dir` in `local.properties`).

## License

Kiji is licensed under the [GNU General Public License v3.0](LICENSE), the same license as Read You. Original copyright notices are preserved.
