package pl.lemanski.pandamidi.soundFont

import pl.lemanski.pandamidi.soundFont.internal.SoundFontDelegate

fun soundFont(path: String): SoundFont {
    loadFromFile(path)
    return object : SoundFont by SoundFontDelegate() { }
}

fun soundFont(memory: ByteArray): SoundFont {
    loadFromMemory(memory)
    return object : SoundFont by SoundFontDelegate() { }
}

internal expect fun loadFromFile(path: String)

internal expect fun loadFromMemory(memory: ByteArray)
