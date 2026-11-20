package me.ash.reader.domain.model.general

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VersionTest {

    @Test
    fun tagWithVPrefixParsesLikeThePlainNumber() {
        assertEquals("1.1.0", "v1.1.0".toVersion().toString())
    }

    @Test
    fun newerTagWithVPrefixNeedsUpdate() {
        assertTrue("v1.1.0".toVersion().whetherNeedUpdate("1.0.0".toVersion(), Version()))
    }

    @Test
    fun sameVersionDoesNotNeedUpdate() {
        assertFalse("v1.0.0".toVersion().whetherNeedUpdate("1.0.0".toVersion(), Version()))
    }

    @Test
    fun skippedVersionDoesNotNeedUpdate() {
        assertFalse("v1.1.0".toVersion().whetherNeedUpdate("1.0.0".toVersion(), "1.1.0".toVersion()))
    }
}
