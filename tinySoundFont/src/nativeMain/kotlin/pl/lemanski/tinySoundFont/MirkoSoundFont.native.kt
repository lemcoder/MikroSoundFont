package pl.lemanski.tinySoundFont

import pl.lemanski.tinySoundFont.internal.getSoundFontDelegate

actual object MikroSoundFont {
    actual fun load(path: String): SoundFont {
        return getSoundFontDelegate(path)
    }

    actual fun load(memory: ByteArray): SoundFont {
        return getSoundFontDelegate(memory)
    }
}

