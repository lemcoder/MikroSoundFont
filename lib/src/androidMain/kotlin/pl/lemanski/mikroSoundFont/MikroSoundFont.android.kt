package pl.lemanski.mikroSoundFont

import pl.lemanski.mikroSoundFont.internal.getSoundFontDelegate

actual object MikroSoundFont {
    actual fun load(path: String): SoundFont = getSoundFontDelegate(path)

    actual fun load(memory: ByteArray): SoundFont = getSoundFontDelegate(memory)
}