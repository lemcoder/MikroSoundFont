package pl.lemanski.pandamidi.soundFont

import pl.lemanski.pandamidi.soundFont.internal.SoundFontDelegate

actual fun soundFont(path: String): SoundFont {
    return SoundFontDelegate(path)
}

actual fun soundFont(memory: ByteArray): SoundFont {
    return SoundFontDelegate(memory)
}