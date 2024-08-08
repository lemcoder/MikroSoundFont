package pl.lemanski.pandamidi.soundFont

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import tinySoundFont.tsf_load_filename
import tinySoundFont.tsf_load_memory

@OptIn(ExperimentalForeignApi::class)
internal actual fun loadFromFile(path: String): Sf {
    return tsf_load_filename(path) ?: throw IllegalStateException("SoundFont not loaded")
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun loadFromMemory(memory: ByteArray): Sf {
    return tsf_load_memory(memory.refTo(0), memory.size) ?: throw IllegalStateException("SoundFont not loaded")
}

