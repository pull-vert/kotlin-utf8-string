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
        val utf8String = bytes.toUtf8String()
        assertFalse(utf8String.isEmpty())
        val utf8Byte = utf8String[0]
        assertTrue(utf8Byte.isAscii)
        assertEquals('a', utf8Byte.toChar())
        assertEquals(1, utf8String.size)
    }

    @Test
    fun verifySingleAsciiCharCodePoint() {
        val bytes = byteArrayOf(97)
        val utf8String = bytes.toUtf8String()
        for ((index, codePoint) in utf8String.withCodePointIndex()) {
            assertEquals(0, index)
            assertEquals('a', codePoint.toChar())
        }
    }

    @Test
    fun sample() {
        // Array containing invalid utf-8
        val bytes = byteArrayOf(0xbd.toByte(), 0xb2.toByte(), 0x3d.toByte(), 0xbc.toByte(), 0x20.toByte(), 0xe2.toByte(),
                0x8c.toByte(), 0x98.toByte())
        val utf8String = bytes.toUtf8String()
        println("Utf8String.toString() return all CodePoints in the UTF-8 byte sequence")
        println(utf8String)
        println()

        println("Print each Utf8Byte")
        for (utf8Byte in utf8String) {
            println("$utf8Byte")
        }
        println()

        println("Print each Utf8Byte hex value")
        for (utf8Byte in utf8String) {
            print("${utf8Byte.toHex()} ")
        }
        println()
        println()

        println("Translate a char to a CodePoint")
        println('⌘'.toCodePoint())
        println()

        println("Use for range to iterate on CodePoints of a Utf8String")
        val nihongo = "日本語".toUtf8String()
        for ((index, codePoint) in nihongo.withCodePointIndex()) {
            println("$codePoint starts at byte position $index")
        }
        println()

        println("Use Utf8String.codePointAt(index) to iterate on CodePoints of a Utf8String")
        var index = 0
        while (index < nihongo.size) {
            val (codePoint, size) = nihongo.codePointAt(index)
            println("$codePoint starts at byte position $index")
            index+=size
        }
    }
}
