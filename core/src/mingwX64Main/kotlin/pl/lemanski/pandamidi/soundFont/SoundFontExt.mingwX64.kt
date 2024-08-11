package pl.lemanski.pandamidi.soundFont

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import pl.lemanski.pandamidi.soundFont.internal.SoundFontHolder
import tinySoundFont.tsf_load_filename
import tinySoundFont.tsf_load_memory

@OptIn(ExperimentalForeignApi::class)
internal actual fun loadFromFile(path: String) {
    SoundFontHolder.tsf = tsf_load_filename(path) ?: throw IllegalStateException("SoundFont not loaded")
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun loadFromMemory(memory: ByteArray) {
    SoundFontHolder.tsf = tsf_load_memory(memory.refTo(0), memory.size) ?: throw IllegalStateException("SoundFont not loaded")
}

