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
        for ((index, codePoint) in stringImpl.withCodePointIndex()) {
            assertEquals(0, index)
            assertEquals('a', codePoint.toChar())
        }
    }

    @Test
    fun alefzef() {
        val bytes = byteArrayOf(0xbd.toByte(), 0xb2.toByte(), 0x3d.toByte(), 0xbc.toByte(), 0x20.toByte(), 0xe2.toByte(),
                0x8c.toByte(), 0x98.toByte())
        val utf8String = Utf8String(bytes)
        println(utf8String)
    }
}
