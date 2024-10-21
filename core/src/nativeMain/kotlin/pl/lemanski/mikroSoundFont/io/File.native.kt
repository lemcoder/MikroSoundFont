package pl.lemanski.mikroSoundFont.io

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import platform.posix.*

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
actual fun loadFile(path: String): ByteArray {
    val file = fopen(path, "r") ?: throw IllegalArgumentException("Could not open file")
    try {
        fseek(file, 0, SEEK_END)
        val fileSize = ftell(file).toInt()
        rewind(file)
        val buffer = ByteArray(fileSize)

        memScoped {
            val bytesRead = fread(buffer.refTo(0), 1.convert(), fileSize.convert(), file).toInt()
            if (bytesRead != fileSize) {
                throw IllegalStateException("Failed to read the entire file: $path")
            }
        }

        return buffer
    } finally {
        fclose(file)
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun saveFile(path: String, byteArray: ByteArray) {
    val file = fopen(path, "wb") ?: throw IllegalArgumentException("Could not open file: $path")
    try {
        memScoped {
            val bytesWritten = fwrite(byteArray.refTo(0), 1.convert(), byteArray.size.convert(), file).toInt()
            if (bytesWritten != byteArray.size) {
                throw IllegalStateException("Failed to write the entire file: $path")
            }
        }
    } finally {
        fclose(file)
    }
}