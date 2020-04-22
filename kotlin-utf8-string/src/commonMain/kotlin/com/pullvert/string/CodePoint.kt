/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.pullvert.string

@SinceKotlin("1.3")
public inline class CodePoint
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
@PublishedApi
internal constructor(@PublishedApi internal val data: Int) : Comparable<CodePoint> {

    /**
     * Converts this [CodePoint] value to [Char].
     */
    public inline fun toChar(): Char = data.toChar()

    /**
     * Compares this value with the specified value for order.
     *
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    public override inline operator fun compareTo(other: CodePoint): Int =
            this.toChar().compareTo(other.toChar())

    @ExperimentalUnsignedTypes
    public inline fun toHex(): String = data.toUInt().toString(16)

    public override fun toString(): String = "U+${toHex().toUpperCase()} '${toChar()}'"

    /**
     * Companion object for [CodePoint] class that contains its constructor functions
     */
    public companion object {
        /**
         * Returns an instance for unicode Replacement Character 0xFFFD
         */
        public inline fun replacementCharacter(): CodePoint = CodePoint(0xFFFD)
    }
}

@SinceKotlin("1.3")
public inline fun Char.toCodePoint() : CodePoint = CodePoint(this.toInt())

@SinceKotlin("1.3")
public data class SizedCodePoint(public val codePoint: CodePoint, public val size: Int)

@SinceKotlin("1.3")
public inline fun Utf8String.codePointAt(index: Int) : SizedCodePoint {
    var i = index
    // use the primitive unboxed 'nextUtf8Byte' method
    var byte = get(i).toInt()
    i++
    if (byte and 0x80 == 0) { // ASCII
        return SizedCodePoint(CodePoint(byte), 1)
    }
    // first unicode byte
    var byteCount: Int
    var value: Int
    when {
        byte < 0x80 -> {
            return SizedCodePoint(CodePoint(byte), 1)
        }
        byte < 0xC0 -> {
            return SizedCodePoint(CodePoint.replacementCharacter(), 1)
        }
        byte < 0xE0 -> {
            byteCount = 1
            value = byte and 0x3F
        }
        byte < 0xF0 -> {
            byteCount = 2
            value = byte and 0x1F
        }
        byte < 0xF8 -> {
            byteCount = 3
            value = byte and 0xF
        }
        else -> return SizedCodePoint(CodePoint.replacementCharacter(), 1)
    }
    val size = byteCount + 1
    while (byteCount > 0) {
        byte = get(i).toInt()
        i++
        // trailing unicode byte
        value = (value shl 6) or (byte and 0x7f)
        byteCount--
    }

    return if (value ushr 16 == 0) {
        SizedCodePoint(CodePoint(value), size)
    } else {
        if (value > MaxCodePoint) {
            SizedCodePoint(CodePoint.replacementCharacter(), size)
        } else {
            // fixme : what to do with lowSurrogate ?
            SizedCodePoint(CodePoint(highSurrogate(value)), size)
        }
    }
}

