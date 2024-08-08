package pl.lemanski.pandamidi.soundFont

import pl.lemanski.pandamidi.soundFont.internal.SoundFontDelegate

internal typealias Sf = Any

fun SoundFont.load(path: String): SoundFont {
    val soundFont = loadFromFile(path)
    return object : SoundFont by SoundFontDelegate(soundFont) { }
}

fun SoundFont.load(memory: ByteArray): SoundFont {
    val soundFont = loadFromMemory(memory)
    return object : SoundFont by SoundFontDelegate(soundFont) { }
}

internal expect fun loadFromFile(path: String): Sf

internal expect fun loadFromMemory(memory: ByteArray): Sf
