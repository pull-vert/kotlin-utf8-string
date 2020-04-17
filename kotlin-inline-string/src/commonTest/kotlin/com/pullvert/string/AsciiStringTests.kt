/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

import kotlin.test.Test
import kotlin.test.assertEquals

class AsciiStringTests {

    @Test
    fun verifyCharAt() {
        val bytes = byteArrayOf(97)
        val stringImpl = bytes.toAsciiString()
        assertEquals('a', stringImpl[0])
        assertEquals(1, stringImpl.length)
    }
}