package io.github.lemcoder.mikrosoundfont

import io.github.lemcoder.mikrosoundfont.internal.getSoundFontDelegate

interface SoundFontLoader {
    fun load(memory: ByteArray): SoundFont
}

object MikroSoundFont : SoundFontLoader {
    override fun load(memory: ByteArray): SoundFont = getSoundFontDelegate(memory)
}
