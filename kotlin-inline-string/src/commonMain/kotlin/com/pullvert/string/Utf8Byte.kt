/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.pullvert.string

@SinceKotlin("1.3")
public inline class Utf8Byte
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
@PublishedApi
internal constructor(@PublishedApi internal val data: Byte) : Comparable<Utf8Byte> {

    public companion object {
        /**
         * A constant holding the min value an ASCII Utf8Byte can have.
         */
        public const val MIN_ASCII_VALUE: Byte = 0x00
        /**
         * A constant holding the max value an ASCII Utf8Byte can have.
         */
        public const val MAX_ASCII_VALUE: Byte = 0x7F
    }

    /**
     * Converts this [Utf8Byte] value to [Byte].
     *
     * The resulting `Byte` value has the same binary representation as this `Utf8Byte` value.
     */
    public inline fun toByte(): Byte = data

    public inline fun toInt(): Int = data.toInt() and 0xff

    /**
     * Converts this [Utf8Byte] value to [Char].
     */
    public inline fun toChar(): Char = toInt().toChar()

    public inline val isAscii: Boolean get() = toInt() and 0x80 == 0

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    public override inline operator fun compareTo(other: Utf8Byte): Int =
            this.toChar().compareTo(other.toChar())

    public override fun toString(): String =
            if (isAscii) {
                "ASCII char, byte value=$data, char value=\'{${toChar()}\'"
            } else {
                "Not ASCII char, byte value=$data"
            }
}

/**
 * Converts this [Byte] value to [Utf8Byte].
 *
 * The resulting `Utf8Byte` value has the same binary representation as this `Byte` value.
 */
@SinceKotlin("1.3")
internal inline fun Byte.toUtf8Byte(): Utf8Byte = Utf8Byte(this)
