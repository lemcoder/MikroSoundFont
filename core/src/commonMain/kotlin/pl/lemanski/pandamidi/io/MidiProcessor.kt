package pl.lemanski.pandamidi.io

/**
 * Represents a MIDI processor that can process MIDI bytes and generate audio data.
 */
interface MidiProcessor : AutoCloseable {
    /**
     * Sets the callback function to be called when audio data is available.
     */
    fun setAudioCallback(callback: (ByteArray) -> Unit)

    /**
     * Sets the sound font from a path.
     */
    fun setSoundFontFromPath(soundFontPath: String)

    /**
     * Processes MIDI bytes and generates audio data.
     */
    suspend fun processMidiBytes(midiBytes: ByteArray)
}

expect fun getMidiProcessor(): MidiProcessor