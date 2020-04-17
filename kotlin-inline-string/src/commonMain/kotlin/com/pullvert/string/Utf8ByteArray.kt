/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package com.pullvert.string

/*@SinceKotlin("1.3")
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
            val i = 12
            val b: Byte = i.toByte()
            storage[index] = value.data
        }

        /** Returns the number of elements in the array. */
        public override val size: Int get() = storage.size

        /** Creates an iterator over the elements of the array. */
        public override operator fun iterator(): UIntIterator = Iterator(storage)

        private class Iterator(private val array: IntArray) : UIntIterator() {
            private var index = 0
            override fun hasNext() = index < array.size
            override fun nextUInt() = if (index < array.size) array[index++].toUInt() else throw NoSuchElementException(index.toString())
        }

        override fun contains(element: UInt): Boolean {
            // TODO: Eliminate this check after KT-30016 gets fixed.
            // Currently JS BE does not generate special bridge method for this method.
            if ((element as Any?) !is UInt) return false

            return storage.contains(element.toInt())
        }

        override fun containsAll(elements: Collection<UInt>): Boolean {
            return (elements as Collection<*>).all { it is UInt && storage.contains(it.toInt()) }
        }

        override fun isEmpty(): Boolean = this.storage.size == 0
    }
 */