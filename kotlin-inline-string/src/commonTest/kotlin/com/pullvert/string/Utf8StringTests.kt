/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Utf8StringTests {

    @Test
    fun verifySingleAsciiChar() {
        val bytes = byteArrayOf(97)
        val stringImpl = bytes.toUtf8String()
        assertFalse(stringImpl.isEmpty())
        val utf8Byte = stringImpl[0]
        assertTrue(utf8Byte.isAscii)
        assertEquals('a', utf8Byte.toChar())
        assertEquals(1, stringImpl.size)
    }

    @Test
    fun verifySingleAsciiCharCodePoint() {
        val bytes = byteArrayOf(97)
        val stringImpl = bytes.toUtf8String()
        stringImpl.forEachCodePointIndexed { index, codePoint ->
            assertEquals(0, index)
            assertEquals('a', codePoint.toChar())
        }
    }
}
