package pl.lemanski.tinySoundFont

import pl.lemanski.tinySoundFont.internal.SoundFontDelegate

actual fun soundFont(path: String): SoundFont {
    return SoundFontDelegate(path)

}

actual fun soundFont(memory: ByteArray): SoundFont {
    return SoundFontDelegate(memory)
}