package pl.lemanski.mikroSoundFont.generator

interface Generator {
    fun setSoundFont(path: String)
    fun generate(midiMessage: MidiMessage): FloatArray
}

expect fun getGenerator(): Generator