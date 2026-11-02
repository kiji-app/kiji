package me.ash.reader.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

private fun icon(name: String, vararg paths: String): ImageVector =
    ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        )
        .apply {
            paths.forEach { addPath(PathParser().parsePathString(it).toNodes(), fill = SolidColor(Color.Black)) }
        }
        .build()

object RokkuIcons {
    val LibraryOutline =
        icon(
            "LibraryOutline",
            "M4.063,21.948h14v-2h-14v-14h-2v14C2.063,21.048 2.963,21.948 4.063,21.948z",
            "M7.937,18.052h12c1.1,0 2,-0.9 2,-2v-12c0,-1.1 -0.9,-2 -2,-2h-12c-1.1,0 -2,0.9 -2,2v12C5.937,17.151 6.837,18.052 7.937,18.052zM8.02,4.011h5.168l0.006,8.014l2.5,-1.5l2.5,1.5v-8V4.011h1.826v12h-12V4.011z",
        )

    val LibraryFilled =
        icon(
            "LibraryFilled",
            "M19.9,2.1h-0.2l-0.8,0V10l-2.5,-1.4l-2.5,1.5v-8l-0.3,0H7.9c-1.1,0 -2,0.9 -2,2v12c0,1.1 0.9,2 2,2h12c1.1,0 2,-0.9 2,-2v-12C21.9,3 21,2.1 19.9,2.1zM4.1,5.9h-2v14c0,1.1 0.9,2 2,2h14v-2h-14V5.9z",
        )

    val BrowseOutline =
        icon(
            "BrowseOutline",
            "M7,17L10.2,10.2L17,7L13.8,13.8L7,17M12,11.1A0.9,0.9 0 0,0 11.1,12A0.9,0.9 0 0,0 12,12.9A0.9,0.9 0 0,0 12.9,12A0.9,0.9 0 0,0 12,11.1M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2M12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12A8,8 0 0,0 12,4Z",
        )

    val BrowseFilled =
        icon(
            "BrowseFilled",
            "M12,10.9c-0.61,0 -1.1,0.49 -1.1,1.1s0.49,1.1 1.1,1.1c0.61,0 1.1,-0.49 1.1,-1.1s-0.49,-1.1 -1.1,-1.1zM12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM14.19,14.19L6,18l3.81,-8.19L18,6l-3.81,8.19z",
        )

    val Settings =
        icon(
            "Settings",
            "M12,9c-1.7,0 -3,1.3 -3,3s1.3,3 3,3s3,-1.3 3,-3S13.7,9 12,9M10,22c-0.2,0 -0.5,-0.2 -0.5,-0.4l-0.4,-2.6c-0.6,-0.2 -1.2,-0.6 -1.7,-1l-2.5,1c-0.2,0.1 -0.5,0 -0.6,-0.2l-2,-3.5c-0.1,-0.2 -0.1,-0.5 0.1,-0.6L4.6,13l-0.1,-1l0.1,-1L2.5,9.4C2.3,9.2 2.2,8.9 2.3,8.7l2,-3.5C4.5,5.1 4.7,5 4.9,5.1l2.5,1c0.5,-0.4 1.1,-0.7 1.7,-1l0.4,-2.7C9.5,2.2 9.8,2 10,2h4c0.2,0 0.5,0.2 0.5,0.4l0.4,2.7c0.6,0.2 1.2,0.6 1.7,1l2.5,-1c0.2,-0.1 0.5,0 0.6,0.2l2,3.5c0.1,0.2 0.1,0.5 -0.1,0.6L19.4,11l0.1,1l-0.1,1l2.1,1.6c0.2,0.1 0.2,0.4 0.1,0.6l-2,3.5C19.5,19 19.3,19 19,19l-2.5,-1c-0.5,0.4 -1.1,0.7 -1.7,1l-0.4,2.6c0,0.2 -0.2,0.4 -0.5,0.4H10M11.2,4l-0.4,2.6c-1.2,0.2 -2.3,0.9 -3,1.8l-2.4,-1L4.7,8.6l2.1,1.6c-0.4,1.2 -0.4,2.4 0,3.6l-2.1,1.6l0.8,1.3l2.4,-1c0.8,0.9 1.8,1.5 3,1.8l0.4,2.6h1.5l0.4,-2.6c1.2,-0.2 2.2,-0.9 3,-1.8l2.4,1l0.8,-1.3l-2.1,-1.5c0.4,-1.2 0.4,-2.4 0,-3.6l2.1,-1.6l-0.8,-1.3l-2.4,1c-0.8,-0.9 -1.8,-1.5 -3,-1.8L12.8,4H11.2z",
        )
}
