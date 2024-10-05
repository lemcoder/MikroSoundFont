package pl.lemanski.tinySoundFont

expect object MikroSoundFont {
    fun load(path: String): SoundFont

    fun load(memory: ByteArray): SoundFont
}
