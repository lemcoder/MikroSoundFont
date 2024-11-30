package io.github.lemcoder.mikrosoundfont

import io.github.lemcoder.mikrosoundfont.internal.getSoundFontDelegate

actual object MikroSoundFont {
    actual fun load(path: String): SoundFont {
        return getSoundFontDelegate(path)
    }

    actual fun load(memory: ByteArray): SoundFont {
        return getSoundFontDelegate(memory)
    }
}

