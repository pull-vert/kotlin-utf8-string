# kotlin-utf8-string

[![License: Unlicense](https://img.shields.io/badge/license-Unlicense-blue.svg)](http://unlicense.org/)

kotlin-utf8-string was born as a challenge to provide the smallest memory footprint UTF-8 String to several platforms
(JVM, JS and native) thanks to Kotlin multiplatform and inline classes. It mimics the golang String model.

Inline classes allow adding behavior to the extended type, without any runtime cost :
* a Utf8String is just a primitive UTF-8 byte array at runtime
* a Utf8Byte is just a primitive byte at runtime
* a CodePoint is just a primitive int at runtime

## UTF-8

source : [UTF-8 on wikipedia](https://en.wikipedia.org/wiki/UTF-8)

UTF-8 is a variable width character encoding that fully support all Unicode code points. UTF-8 is defined to encode code
points in one to four bytes, depending on the number of significant bits in the numerical value of the code point.

Code points with lower numerical values, which tend to occur more frequently, are encoded using fewer bytes.

The first 128 characters of Unicode, which correspond one-to-one with ASCII, are encoded using a single byte with the
same binary value as ASCII, so that valid ASCII text is valid UTF-8-encoded Unicode as well.

## use UTF-8 String in Kotlin with kotlin-utf8-string : Utf8String, Utf8Byte and CodePoint

source : this is this [golang blog article](https://blog.golang.org/strings) adapted to kotlin-utf8-string.

### What is a string?

Let's start with some basics.

In kotlin-utf8-string, a Utf8String is just a read-only array of bytes.

It's important to state right up front that a Utf8String holds arbitrary bytes. It is not required to hold Unicode text,
UTF-8 text, or any other predefined format. As far as the content of a Utf8String is concerned, it is exactly equivalent
to an array of bytes.

Here is a Utf8String that uses the 0xNN notation to define a string constant holding a list of byte values. (Of
course, bytes range from hexadecimal values 00 through FF, inclusive.)

```kotlin
val bytes = byteArrayOf(0xbd.toByte(), 0xb2.toByte(), 0x3d.toByte(), 0xbc.toByte(), 0x20.toByte(),
        0xe2.toByte(), 0x8c.toByte(), 0x98.toByte())
val utf8String = bytes.toUtf8String()
```

### Printing strings

Because some of the bytes in our sample string are not valid ASCII, not even valid UTF-8, printing the string directly
will produce ugly output. The simple println statement

```kotlin
println(utf8String)
```

produces this mess (whose exact appearance varies with the environment):
```
��=� ⌘
```

We can see that buried in the noise is one ASCII equals sign, along with a regular space, and at the end appears the
well-known Swedish "Place of Interest" symbol. That symbol has Unicode value U+2318, encoded as UTF-8 by the bytes after
the space (hex value 20): ```e2 8c 98```.

To find out what that string really holds, we need to take it apart and examine the pieces. There are several ways to do
this. The most obvious is to loop over its contents and pull out the bytes individually, as in this for loop:

```kotlin
for (utf8Byte in utf8String) {
    print("${utf8Byte.toHex()} ")
}
```

As implied up front, indexing a string accesses individual bytes, not characters. This is the output from the
byte-by-byte loop:

```
bd b2 3d bc 20 e2 8c 98
```

Notice how the individual bytes match the hexadecimal escapes that defined the string.

These printing techniques are good to know when debugging the contents of strings, and will be handy in the discussion
that follows.

### Code points and characters

We've been very careful so far in how we use the words "byte" and "character". That's partly because strings hold bytes,
and partly because the idea of "character" is a little hard to define. The Unicode standard uses the term "code point"
to refer to the item represented by a single value. The code point U+2318, with hexadecimal value 2318, represents the
symbol ⌘. (For lots more information about that code point, see its Unicode page.)

To pick a more prosaic example, the Unicode code point U+0061 is the lower case Latin letter 'A': a.

But what about the lower case grave-accented letter 'A', à? That's a character, and it's also a code point (U+00E0), but
it has other representations. For example we can use the "combining" grave accent code point, U+0300, and attach it to
the lower case letter a, U+0061, to create the same character à. In general, a character may be represented by a number
of different sequences of code points, and therefore different sequences of UTF-8 bytes.

The concept of character in computing is therefore ambiguous, or at least confusing, so we use it with care. To make
things dependable, there are normalization techniques that guarantee that a given character is always represented by the
same code points, but that subject takes us too far off the topic.
**kotlin-utf8-string do not offer normalization** for now.

kotlin-utf8-string defines the ```CodePoint``` type as an inline class that wraps an Int, so programs can be clear when
an integer value represents a code point. Moreover, you can easily get the CodePoint from a character constant as follow

```kotlin
println('⌘'.toCodePoint())
```

Will output the unicode hexadecimal value and the character :

```U+2318 '⌘'```

To summarize, here are the salient points:
* A Utf8String holds arbitrary bytes.
* A valid UTF-8 byte sequence represent a sequence of Unicode ```CodePoint```s.
* No guarantee is made in kotlin-utf8-string that characters in strings are normalized.

### Range loops

There's really only one way that kotlin-utf8-string treats UTF-8 specially, and that is when using a specific for
range loop on a string.

We've seen what happens with a regular for loop. A for range loop, by contrast, decodes one UTF-8-encoded CodePoint on
each iteration. Each time around the loop, the index of the loop is the starting position of the current CodePoint,
measured in bytes, and the code point is its value. Here's an example :

```kotlin
val nihongo = "日本語".toUtf8String()
for ((index, codePoint) in nihongo.withCodePointIndex()) {
    println("$codePoint starts at byte position $index")
}
```

The output shows how each code point occupies multiple bytes:

```
U+65E5 '日' starts at byte position 0
U+672C '本' starts at byte position 3
U+8A9E '語' starts at byte position 6
```

### Other decode function

If a for range loop isn't sufficient for your purposes, here is a program equivalent to the for range example above, but
using the ```codePointAt``` function to do the work. The return values from this function are the CodePoint and its
size in UTF-8-encoded bytes.

```kotlin
val nihongo = "日本語".toUtf8String()
var index = 0
while (index < nihongo.size) {
    val (codePoint, size) = nihongo.codePointAt(index)
    println("$codePoint starts at byte position $index")
    index+=size
}
```

The output shows that it performs the same:

```
U+65E5 '日' starts at byte position 0
U+672C '本' starts at byte position 3
U+8A9E '語' starts at byte position 6
```

The for range loop and ```codePointAt``` are defined to produce exactly the same iteration sequence.

### Conclusion

To answer the question posed at the beginning: Utf8String is built from bytes so iteration on it yields bytes, not
characters. A ```Utf8String``` might not even hold characters. In fact, the definition of "character" is ambiguous, and it
would be a mistake to try to resolve the ambiguity by defining that strings are made of characters.

There's much more to say about Unicode, UTF-8, and the world of multilingual text processing, but it is enough for now.
We hope you have a better understanding of how kotlin-utf8-string ```Utf8String``` behave and that, although it may
contain arbitrary bytes, UTF-8 is a central part of its design.

## Full sample

```kotlin
// Array containing invalid utf-8
val bytes = byteArrayOf(0xbd.toByte(), 0xb2.toByte(), 0x3d.toByte(), 0xbc.toByte(), 0x20.toByte(), 0xe2.toByte(),
        0x8c.toByte(), 0x98.toByte())
val utf8String = bytes.toUtf8String()
println("Utf8String.toString() return all CodePoints in the UTF-8 byte sequence")
println(utf8String)
println()

println("Print each Utf8Byte")
for (utf8Byte in utf8String) {
    println("$utf8Byte")
}
println()

println("Print each Utf8Byte hex value")
for (utf8Byte in utf8String) {
    print("${utf8Byte.toHex()} ")
}
println()
println()

println("Translate a char to a CodePoint")
println('⌘'.toCodePoint())
println()

println("Use for range to iterate on CodePoints of a Utf8String")
val nihongo = "日本語".toUtf8String()
for ((index, codePoint) in nihongo.withCodePointIndex()) {
    println("$codePoint starts at byte position $index")
}
println()

println("Use Utf8String.codePointAt(index) to iterate on CodePoints of a Utf8String")
var index = 0
while (index < nihongo.size) {
    val (codePoint, size) = nihongo.codePointAt(index)
    println("$codePoint starts at byte position $index")
    index+=size
}
```

The output is:

```
Utf8String.toString() return all CodePoints in the UTF-8 byte sequence
��=� ⌘

Print each Utf8Byte
0xBD not ASCII
0xB2 not ASCII
0x3D '='
0xBC not ASCII
0x20 ' '
0xE2 not ASCII
0x8C not ASCII
0x98 not ASCII

Print each Utf8Byte hex value
bd b2 3d bc 20 e2 8c 98 

Translate a char to a CodePoint
U+2318 '⌘'

Use for range to iterate on CodePoints of a Utf8String
U+65E5 '日' starts at byte position 0
U+672C '本' starts at byte position 3
U+8A9E '語' starts at byte position 6

Use Utf8String.codePointAt(index) to iterate on CodePoints of a Utf8String
U+65E5 '日' starts at byte position 0
U+672C '本' starts at byte position 3
U+8A9E '語' starts at byte position 6
```
