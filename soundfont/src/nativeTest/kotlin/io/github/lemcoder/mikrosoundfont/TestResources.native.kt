package io.github.lemcoder.mikrosoundfont

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.getenv

/** The Gradle build points this variable at `src/commonTest/resources`. */
private const val RESOURCES_ENV = "MIKROSOUNDFONT_TEST_RESOURCES"

@OptIn(ExperimentalForeignApi::class)
actual fun readTestResource(name: String): ByteArray {
    val dir = getenv(RESOURCES_ENV)?.toKString() ?: error("$RESOURCES_ENV is not set")
    val file = fopen("$dir/$name", "rb") ?: error("Cannot open $dir/$name")
    try {
        fseek(file, 0, SEEK_END)
        val size = ftell(file).toInt()
        fseek(file, 0, SEEK_SET)
        val bytes = ByteArray(size)
        val read = bytes.usePinned { fread(it.addressOf(0), 1u, size.toULong().convert(), file) }
        check(read.toLong() == size.toLong()) { "Short read of $name" }
        return bytes
    } finally {
        fclose(file)
    }
}
