package pl.lemanski.tinySoundFont

/**
 * This class represents a channel on a soundfont.
 */
interface Channel {
    /**
     * The channel number
     */

    val number: Int

    /**
     * Set preset index >= 0 and < [preset count]
     */
    fun setPresetIndex(presetIndex: Int)

    /**
     * Set preset number (alternative to presetIndex)
     */
    fun setPresetNumber(presetNumber: Int, isMidiDrums: Boolean)

    /**
     * Set instrument bank number (alternative to presetIndex)
     */
    fun setBank(bank: Int)

    /**
     * Set instrument bank and preset number
     */
    fun setBankPreset(bank: Int, presetNumber: Int)

    /**
     * Set stereo panning value from 0.0 (left) to 1.0 (right) (default 0.5 center)
     */
    fun setPan(pan: Float)

    /**
     * Set linear volume scale factor (default 1.0 full)
     */
    fun setVolume(volume: Float)

    /**
     * Set pitch wheel position 0 to 16383 (default 8192 unpitched)
     */
    fun setPitchWheel(pitchWheel: Int)

    /**
     * Set range of the pitch wheel in semitones (default 2.0, total +/- 2 semitones)
     */
    fun setPitchRange(pitchRange: Float)

    /**
     * Set tuning of all playing voices in semitones (default 0.0, standard (A440) tuning)
     */
    fun setTuning(tuning: Float)


    /**
     * Start playing a note
     */
    fun noteOn(key: Int, velocity: Float)

    /**
     * Stop playing a note
     */
    fun noteOff(key: Int)

    /**
     * Stop playing all notes (end with sustain and release)
     */
    fun noteOffAll()

    /**
     * Stop playing all notes (end immediately)
     */
    fun soundsOffAll()


    /**
     * Get current preset index set on the channel
     */
    fun getPresetIndex(): Int

    /**
     * Get current instrument bank set on the channel
     */
    fun getPresetBank(): Int

    /**
     * Get current preset number set on the channel
     */
    fun getPresetNumber(): Int

    /**
     * Get current stereo panning value from 0.0 (left) to 1.0 (right)
     */
    fun getPan(): Float

    /**
     * Get current linear volume scale factor
     */
    fun getVolume(): Float

    /**
     * Get current pitch wheel position
     */
    fun getPitchWheel(): Int

    /**
     * Get current range of the pitch wheel in semitones
     */
    fun getPitchRange(): Float

    /**
     * Get current tuning of all playing voices in semitones
     */
    fun getTuning(): Float
}