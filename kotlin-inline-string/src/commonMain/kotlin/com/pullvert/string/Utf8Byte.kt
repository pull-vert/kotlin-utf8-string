/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.pullvert.string

@SinceKotlin("1.3")
@PublishedApi
internal inline class Utf8Byte
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
        public const val MAX_ASCII_VALUE: Byte = 0xFF.toByte()
    }

    /**
     * Converts this [Utf8Byte] value to [Byte].
     *
     * The resulting `Byte` value has the same binary representation as this `Utf8Byte` value.
     */
    public inline fun toByte(): Byte = data

    /**
     * Converts this [Utf8Byte] value to [Char].
     *
     * In kotlin byte IS a byte -> there is no need to mask out the higher bits ('X and 0xFF'), as they are already zero
     */
    public inline fun toChar(): Char = data.toChar()

    public val isValidAscii: Boolean get() = data > MIN_ASCII_VALUE

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    public override inline operator fun compareTo(other: Utf8Byte): Int = this.data.compareTo(other.data)

    public override fun toString(): String =
            if (isValidAscii) {
                "valid ascii char, byte value=$data, char value=\'{${toChar()}\'"
            } else {
                "invalid ascii char, byte value=$data"
            }
}

/**
 * Converts this [Byte] value to [Utf8Byte].
 *
 * The resulting `Utf8Byte` value has the same binary representation as this `Byte` value.
 */
@SinceKotlin("1.3")
internal inline fun Byte.toUtf8Byte(): Utf8Byte = Utf8Byte(this)
