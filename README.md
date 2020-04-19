# kotlin-inline-string

kotlin-inline-string was born as a challenge to provide the smallest memory footprint UTF-8 String to several platforms
(JVM, JS and native) thanks to Kotlin multiplatform and inline classes. It mimics the golang String model.

Inline classes allow adding behavior to the extended type, without any runtime cost :
* a Utf8String is just a primitive UTF-8 byte array at runtime
* a Utf8Byte is just a primitive byte at runtime
* a CodePoint is just a primitive int at runtime

## UTF-8

source : https://en.wikipedia.org/wiki/UTF-8

UTF-8 is a variable width character encoding that fully support all Unicode code points. UTF-8 is defined to encode code
points in one to four bytes, depending on the number of significant bits in the numerical value of the code point.

Code points with lower numerical values, which tend to occur more frequently, are encoded using fewer bytes.

The first 128 characters of Unicode, which correspond one-to-one with ASCII, are encoded using a single byte with the
same binary value as ASCII, so that valid ASCII text is valid UTF-8-encoded Unicode as well.

## use UTF-8 String in Kotlin with kotlin-inline-string : Utf8String, Utf8Byte and CodePoint

source : this is this [golang blog article](https://blog.golang.org/strings) adapted to kotlin-inline-string.

### What is a string?

Let's start with some basics.

In kotlin-inline-string, a Utf8String is in effect a read-only array of bytes.

It's important to state right up front that a Utf8String holds arbitrary bytes. It is not required to hold Unicode text,
UTF-8 text, or any other predefined format. As far as the content of a Utf8String is concerned, it is exactly equivalent
to an array of bytes.

Here is a Utf8String that uses the \xNN notation to define a string constant holding some peculiar byte values. (Of
course, bytes range from hexadecimal values 00 through FF, inclusive.)
```kotlin

```

## A few notes

**restriction** : hashcode/equals in inline class not supported in 1.3.72 (nor 1.4-M1)
