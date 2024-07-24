package pl.lemanski.pandamidi.io

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toCValues
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite

@OptIn(ExperimentalForeignApi::class)
actual fun WavFile.save(name: String) {
    val file = fopen(name, "w")
    val headerBytes = header.toByteArray()
    fwrite(headerBytes.toCValues(), headerBytes.size.toULong(), headerBytes.size.toULong(), file)
    fwrite(data.refTo(0), 2.toULong(), data.size.toULong(), file)
    fclose(file)
}