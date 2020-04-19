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

    // companion with constructors

    /**
     * Companion object for [Result] class that contains its constructor functions
     * [success] and [failure].
     */
    public companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        public inline fun oneByte(value: Int): CodePoint = CodePoint(value)

        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        public inline fun replacementCharacter(): CodePoint = CodePoint(0xFFFD)
    }
}