/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

import kotlin.math.min

/**
 * An immutable inline class that store a String as an UTF-8 encoded [ByteArray] in heap-memory
 */
@SinceKotlin("1.3")
public inline class Utf8String
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
@PublishedApi
internal constructor(@PublishedApi internal val storage: ByteArray) : Collection<Utf8Byte>, Comparable<Utf8String> {

    /**
     * Returns the array element at the given [index]. This method can be called using the index operator.
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    public operator fun get(index: Int): Utf8Byte = storage[index].toUtf8Byte()

    /**
     * Returns the byte-length in the byte array
     *
     * This size may not be the same as the number of chars in this String as an UTF-8 char needs between one to 4 bytes
     * to encode in UTF-8
     */
    public override val size: Int get() = storage.size

    /** Creates an iterator over the elements of the array. */
    public override operator fun iterator(): Utf8ByteIterator = Iterator(storage)

    private class Iterator internal constructor(private val array: ByteArray) : Utf8ByteIterator() {
        private var index = 0
        override fun hasNext(): Boolean = index < array.size
        override fun nextUtf8Byte(): Utf8Byte =
                if (index < array.size) {
                    array[index++].toUtf8Byte()
                } else {
                    throw NoSuchElementException(index.toString())
                }
    }

    override fun contains(element: Utf8Byte): Boolean {
        // TODO: Eliminate this check after KT-30016 gets fixed.
        // Currently JS BE does not generate special bridge method for this method.
        @Suppress("USELESS_CAST")
        if ((element as Any?) !is Utf8Byte) return false

        return storage.contains(element.toByte())
    }

    override fun containsAll(elements: Collection<Utf8Byte>): Boolean {
        return (elements as Collection<*>).all { it is Utf8Byte && storage.contains(it.toByte()) }
    }

    override fun isEmpty(): Boolean = this.storage.size == 0

    override fun compareTo(other: Utf8String): Int {
        val len1 = this.size
        val len2 = other.size
        val lim: Int = min(len1, len2)
        for (k in 0 until lim) {
            if (this[k] != other[k]) {
                return this[k].compareTo(other[k])
            }
        }
        return len1 - len2
    }

    public override fun toString(): String {
        val sb = StringBuilder()
        for ((_, codePoint) in this.withCodePointIndex()) {
            sb.append(codePoint.toChar())
        }
        return sb.toString()
    }
}

/**
 * Converts this UTF-8 encoded [ByteArray] to [Utf8String].
 *
 * The resulting `Utf8String` value has the same binary representation as this `ByteArray` value.
 */
@SinceKotlin("1.3")
@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.toUtf8String(): Utf8String = Utf8String(this)

@SinceKotlin("1.3")
@Suppress("NOTHING_TO_INLINE")
@ExperimentalStdlibApi
public inline fun String.toUtf8String(): Utf8String = Utf8String(this.encodeToByteArray())


