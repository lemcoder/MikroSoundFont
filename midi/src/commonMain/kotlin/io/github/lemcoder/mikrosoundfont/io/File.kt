package io.github.lemcoder.mikrosoundfont.io

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

fun loadFile(path: String): ByteArray {
    if (!SystemFileSystem.exists(Path(path))) {
        return byteArrayOf()
    }

    return SystemFileSystem.source(Path(path)).buffered().readByteArray()
}

fun saveFile(byteArray: ByteArray, path: String) {
    val p = Path(path)

    if (p.parent?.let { SystemFileSystem.exists(it) } == false) {
        SystemFileSystem.createDirectories(p)
    }

    SystemFileSystem.sink(p).buffered().write(byteArray)
}