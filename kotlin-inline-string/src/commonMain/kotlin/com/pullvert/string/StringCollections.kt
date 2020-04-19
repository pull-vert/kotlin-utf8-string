/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

/** An iterator over a sequence of values of type `Utf8Byte`. */
@SinceKotlin("1.3")
public abstract class Utf8ByteIterator internal constructor() : Iterator<Utf8Byte> {
    final override fun next(): Utf8Byte = nextUtf8Byte()

    /** Returns the next value in the sequence without boxing. */
    public abstract fun nextUtf8Byte(): Utf8Byte
}

/**
 * Performs the given [action] on each element, providing sequential index with the element.
 * @param [action] function that takes the index of an element and the element itself
 * and performs the desired action on the element.
 */
@SinceKotlin("1.3")
public inline fun Utf8String.forEachCodePointIndexed(action: (index: Int, CodePoint) -> Unit): Unit {
    var index = 0
    val iterator = this.iterator()
    var utf8Byte: Utf8Byte
    while (iterator.hasNext()) {
        // use the primive unboxed 'nextUtf8Byte' method
        utf8Byte = iterator.nextUtf8Byte()
        when {
            utf8Byte.isAscii -> action(index++, CodePoint.oneByte(utf8Byte.data))
            else -> throw TODO() // incorrect UTF-8 Byte
        }
    }
}

/**
 * Returns a lazy [Iterable] that wraps each element of the original collection
 * into an [IndexedValue] containing the index of that element and the element itself.
 */
@SinceKotlin("1.3")
public fun Utf8String.withCodePointIndex(): Iterable<IndexedCodePoint> {
    return IndexingCodePointIterable { iterator() }
}

/**
 * A wrapper over another [Iterable] (or any other object that can produce an [Iterator]) that returns
 * an indexing iterator.
 */
internal class IndexingCodePointIterable(
        private val iteratorFactory: () -> Utf8ByteIterator
) : Iterable<IndexedCodePoint> {
    override fun iterator(): Iterator<IndexedCodePoint> = IndexingCodePointIterator(iteratorFactory())
}

/**
 * Iterator transforming original `iterator` into iterator of [IndexedValue], counting index from zero.
 */
internal class IndexingCodePointIterator(private val iterator: Utf8ByteIterator) : Iterator<IndexedCodePoint> {
    private var index = 0
    override fun hasNext(): Boolean = iterator.hasNext()
    override fun next(): IndexedCodePoint {
        // use the primive unboxed 'nextUtf8Byte' method
        val utf8Byte = iterator.nextUtf8Byte()
        return when {
            utf8Byte.isAscii -> IndexedCodePoint(index++, CodePoint.oneByte(utf8Byte.data))
            else -> throw TODO() // incorrect UTF-8 Byte
        }
    }
}

public data class IndexedCodePoint(public val index: Int, public val codePoint: CodePoint)
