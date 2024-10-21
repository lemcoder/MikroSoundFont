package pl.lemanski.mikroSoundFont.io

import java.io.File

actual fun loadFile(path: String): ByteArray {
    val file = File(path)
    if (!file.exists()) {
        throw IllegalArgumentException("File does not exist at: $path")
    }

    return try {
        file.readBytes()
    } catch (ex: Exception) {
        byteArrayOf()
    }
}

actual fun saveFile(path: String, byteArray: ByteArray) {
    val file = File(path)
    if (file.exists()) {
        file.createNewFile()
    }

    file.writeBytes(byteArray)
}