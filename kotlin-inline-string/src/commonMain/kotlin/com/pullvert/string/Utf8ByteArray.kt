/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

@SinceKotlin("1.3")
@PublishedApi
internal inline class Utf8ByteArray
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
@PublishedApi
internal constructor(@PublishedApi internal val storage: ByteArray) : Collection<Utf8Byte> {

    /**
     * Returns the array element at the given [index]. This method can be called using the index operator.
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    public operator fun get(index: Int): Utf8Byte = storage[index].toUtf8Byte()

    /**
     * Sets the element at the given [index] to the given [value]. This method can be called using the index operator.
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    public operator fun set(index: Int, value: Utf8Byte) {
        storage[index] = value.toByte()
    }

    /** Returns the number of elements in the array. */
    public override val size: Int get() = storage.size

    /** Creates an iterator over the elements of the array. */
    public override operator fun iterator(): Utf8ByteIterator = Iterator(storage)

    private class Iterator(private val array: ByteArray) : Utf8ByteIterator() {
        private var index = 0
        override fun hasNext() = index < array.size
        override fun nextUtf8Byte() =
                if (index < array.size) {
                    array[index++].toUtf8Byte()
                } else {
                    throw NoSuchElementException(index.toString())
                }
    }

    override fun contains(element: Utf8Byte): Boolean {
        // TODO: Eliminate this check after KT-30016 gets fixed.
        // Currently JS BE does not generate special bridge method for this method.
        if ((element as Any?) !is Utf8Byte) return false

        return storage.contains(element.toByte())
    }

    override fun containsAll(elements: Collection<Utf8Byte>): Boolean {
        return (elements as Collection<*>).all { it is Utf8Byte && storage.contains(it.toByte()) }
    }

    override fun isEmpty(): Boolean = this.storage.size == 0
}

/**
 * Converts this [Byte] value to [Utf8Byte].
 *
 * The resulting `Utf8Byte` value has the same binary representation as this `Byte` value.
 */
@SinceKotlin("1.3")
@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun ByteArray.toUtf8ByteArray(): Utf8ByteArray = Utf8ByteArray(this)

/** An iterator over a sequence of values of type `Utf8Byte`. */
@SinceKotlin("1.3")
internal abstract class Utf8ByteIterator : Iterator<Utf8Byte> {
    final override fun next(): Utf8Byte = nextUtf8Byte()

    /** Returns the next value in the sequence without boxing. */
    public abstract fun nextUtf8Byte(): Utf8Byte
}
