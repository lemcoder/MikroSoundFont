package pl.lemanski.pandamidi.soundFont

interface SoundFont {
    val channels: List<Channel>

    /**
     * Stop all playing notes immediately and reset all channel parameters
     */
    fun reset()

    /**
     * Returns the preset index from a bank and preset number, or -1 if it does not exist in the loaded SoundFont
     */
    fun getPresetIndex(bank: Int, presetNumber: Int): Int

    /**
     * Returns the number of presets in the loaded SoundFont
     */
    fun getPresetsCount(): Int

    /**
     * Returns the name of a preset index >= 0 and < tsf_get_presetcount()
     */
    fun getPresetName(presetIndex: Int): String

    /**
     * Returns the name of a preset by bank and preset number
     */
    fun bankGetPresetName(bank: Int, presetNumber: Int): String

    /**
     * Setup the parameters for the voice render methods
     * @param outputMode: if mono or stereo and how stereo channel data is ordered
     * @param sampleRate: the number of samples per second (output frequency)
     * @param globalGainDb: volume gain in decibels (>0 means higher, <0 means lower)
     */
    fun setOutput(outputMode: OutputMode, sampleRate: Int, globalGainDb: Float)

    /**
     * Set the global gain as a volume factor
     * @param globalGain: the desired volume where 1.0 is 100%
     */
    fun setVolume(globalGain: Float)

    /**
     * Set the maximum number of voices to play simultaneously
     * Depending on the soundfont, one note can cause many new voices to be started,
     * so don't keep this number too low or otherwise sounds may not play.
     *
     * @param maxVoices: maximum number to pre-allocate and set the limit to
     * @throws IllegalArgumentException if allocation failed
     */
    fun setMaxVoices(maxVoices: Int)

    /**
     * Start playing a note
     * @param presetIndex preset index >= 0 and < getPresetCount()
     * @param key note value between 0 and 127 (60 being middle C)
     * @param velocity velocity as a float between 0.0 (equal to note off) and 1.0 (full)
     * @throws IllegalArgumentException if allocation of a new voice failed
     */
    fun noteOn(presetIndex: Int, key: Int, velocity: Float)

    /**
     * Start playing a note
     * @param bank instrument bank number (alternative to presetIndex)
     * @param presetNumber preset number (alternative to presetIndex)
     * @param key note value between 0 and 127 (60 being middle C)
     * @param velocity velocity as a float between 0.0 (equal to note off) and 1.0 (full)
     * @throws IllegalArgumentException if preset does not exist or allocation failed
     */
    fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float)

    /**
     * Stop playing a note
     * @param presetIndex preset index >= 0 and < getPresetCount()
     * @param key note value between 0 and 127 (60 being middle C)
     */
    fun noteOff(presetIndex: Int, key: Int)

    /**
     * Stop playing a note
     * @param bank instrument bank number (alternative to presetIndex)
     * @param presetNumber preset number (alternative to presetIndex)
     * @param key note value between 0 and 127 (60 being middle C)
     * @throws IllegalArgumentException if preset does not exist
     */
    fun bankNoteOff(bank: Int, presetNumber: Int, key: Int)

    /**
     * Stop playing all notes (end with sustain and release)
     */
    fun noteOffAll()

    /**
     * @return the number of active voices
     */
    fun activeVoiceCount(): Int

    /**
     * Set up channel parameters
     */
    fun setBankPreset(channel: Int, bank: Int, presetNumber: Int)

    /**
     * Render output samples in F32 format
     * @param isMixing: if false clear the buffer first, otherwise mix into existing data
     */
    fun renderFloat(samples: Int, isMixing: Boolean): FloatArray

    /**
     * Supported output modes by the render methods
     */
    enum class OutputMode {
        /**
         * Two channels with single left/right samples one after another
         */
        TSF_STEREO_INTERLEAVED,

        /**
         * Two channels with all samples for the left channel first then right
         */
        TSF_STEREO_UNWEAVED,

        /**
         * A single channel (stereo instruments are mixed into center)
         */
        TSF_MONO
    }
}