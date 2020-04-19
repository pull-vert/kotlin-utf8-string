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
    var index = 0
    var lowSurrogate = 0
    override fun hasNext(): Boolean = (lowSurrogate > 0) || iterator.hasNext()
    override fun next(): IndexedCodePoint {
        if (lowSurrogate > 0) {
            lowSurrogate = 0
            return IndexedCodePoint(index, CodePoint(lowSurrogate))
        }
        // use the primitive unboxed 'nextUtf8Byte' method
        val codePointIndex = index
        var byte = iterator.nextUtf8Byte().toInt()
        index++
        var value = 0
        if (byte and 0x80 == 0) { // ASCII
            return IndexedCodePoint(codePointIndex, CodePoint(byte))
        }
        // first unicode byte
        var byteCount = 0
        when {
            byte < 0x80 -> {
                return IndexedCodePoint(codePointIndex, CodePoint(byte))
            }
            byte < 0xC0 -> {
                return IndexedCodePoint(codePointIndex, CodePoint.replacementCharacter())
                /*byteCount = 0
                value = byte and 0x7F*/
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
            else -> return IndexedCodePoint(codePointIndex, CodePoint.replacementCharacter())
        }
        while (byteCount > 0) {
            byte = iterator.nextUtf8Byte().toInt()
            index++
            // trailing unicode byte
            value = (value shl 6) or (byte and 0x7f)
            byteCount--
        }

        return if (value ushr 16 == 0) {
            IndexedCodePoint(codePointIndex, CodePoint(value))
        } else {
            if (value > MaxCodePoint) {
                IndexedCodePoint(codePointIndex, CodePoint.replacementCharacter())
            } else {
                this.lowSurrogate = lowSurrogate(value)
                IndexedCodePoint(codePointIndex, CodePoint(highSurrogate(value)))
            }
        }
    }
}

public data class IndexedCodePoint(public val index: Int, public val codePoint: CodePoint)

/**
 * Inline depth optimisation
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun lowSurrogate(codePoint: Int): Int = (codePoint and 0x3ff) + MinLowSurrogate

@Suppress("NOTHING_TO_INLINE")
private inline fun highSurrogate(codePoint: Int): Int = (codePoint ushr 10) + HighSurrogateMagic

private const val MaxCodePoint = 0x10ffff
internal const val MinLowSurrogate = 0xdc00
private const val MinHighSurrogate = 0xd800
private const val MinSupplementary = 0x10000
internal const val HighSurrogateMagic = MinHighSurrogate - (MinSupplementary ushr 10)
