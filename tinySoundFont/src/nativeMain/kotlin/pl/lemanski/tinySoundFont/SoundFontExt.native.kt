package pl.lemanski.tinySoundFont

import pl.lemanski.tinySoundFont.internal.getSoundFontDelegate

actual fun soundFont(path: String): SoundFont {
    return getSoundFontDelegate(path)

}

actual fun soundFont(memory: ByteArray): SoundFont {
    return getSoundFontDelegate(memory)
}