package pl.lemanski.pandamidi.io

interface MidiWavConverter {
    /**
     * Generates a WAV file from a MIDI file and a sound font.
     * @return the path to the generated WAV file
     */
    fun generate(soundFontPath: String, midiFilePath: String): String
}

expect fun getMidiWavConverter(): MidiWavConverter