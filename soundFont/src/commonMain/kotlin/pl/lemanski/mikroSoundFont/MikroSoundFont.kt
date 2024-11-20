package pl.lemanski.mikroSoundFont

expect object MikroSoundFont {
    fun load(path: String): SoundFont

    fun load(memory: ByteArray): SoundFont
}
