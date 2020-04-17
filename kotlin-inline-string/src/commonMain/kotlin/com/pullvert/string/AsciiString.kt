/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

@SinceKotlin("1.3")
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public inline class AsciiString @PublishedApi internal constructor(private val utf8bytes: Utf8ByteArray) : CharSequence {

    public override val length: Int
        get() = utf8bytes.size

    public override fun get(index: Int): Char {
        if (index < 0 || index >= utf8bytes.size) {
            throw IndexOutOfBoundsException("todo")
        }
        return utf8bytes[index].toChar()
    }

    public override fun subSequence(startIndex: Int, endIndex: Int): AsciiString {
        return this
    }
}

@SinceKotlin("1.3")
@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.toAsciiString(): AsciiString = AsciiString(this.toUtf8ByteArray())
