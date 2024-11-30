package io.github.lemcoder.mikrosoundfont

expect object MikroSoundFont {
    fun load(path: String): SoundFont

    fun load(memory: ByteArray): SoundFont
}
