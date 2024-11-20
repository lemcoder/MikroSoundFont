package pl.lemanski.mikroSoundFont.internal

import pl.lemanski.mikroSoundFont.Channel
import pl.lemanski.mikroSoundFont.SoundFont

internal actual fun getChannelDelegate(
    number: Int,
    soundFont: SoundFont
): Channel = ChannelDelegate(number)

internal class ChannelDelegate(
    override val number: Int,
) : Channel {
    override fun setPresetIndex(presetIndex: Int) = setPresetIndex(number, presetIndex)

    override fun setPresetNumber(presetNumber: Int, isMidiDrums: Boolean) = setPresetNumber(number, presetNumber, isMidiDrums)

    override fun setBank(bank: Int) = setBank(number, bank)

    override fun setBankPreset(bank: Int, presetNumber: Int) = setBankPreset(number, bank, presetNumber)

    override fun setPan(pan: Float) = setPan(number, pan)

    override fun setVolume(volume: Float) = setVolume(number, volume)

    override fun setPitchWheel(pitchWheel: Int) = setPitchWheel(number, pitchWheel)

    override fun setPitchRange(pitchRange: Float) = setPitchRange(number, pitchRange)

    override fun setTuning(tuning: Float) = setTuning(number, tuning)

    override fun noteOn(key: Int, velocity: Float) = noteOn(number, key, velocity)

    override fun noteOff(key: Int) = noteOff(number, key)

    override fun noteOffAll() = noteOffAll(number)

    override fun soundsOffAll() = soundsOffAll(number)

    override fun getPresetIndex(): Int = getPresetIndex(number)

    override fun getPresetBank(): Int = getPresetBank(number)

    override fun getPresetNumber(): Int = getPresetNumber(number)

    override fun getPan(): Float = getPan(number)

    override fun getVolume(): Float = getVolume(number)

    override fun getPitchWheel(): Int = getPitchWheel(number)

    override fun getPitchRange(): Float = getPitchRange(number)

    override fun getTuning(): Float = getTuning(number)

    override fun setMidiControl(control: Int, controlValue: Int) = setMidiControl(number, control, controlValue)

    // ---

    private external fun setPresetIndex(channel: Int, presetIndex: Int)

    private external fun setPresetNumber(channel: Int, presetNumber: Int, isMidiDrums: Boolean)

    private external fun setBank(channel: Int, bank: Int)

    private external fun setBankPreset(channel: Int, bank: Int, presetNumber: Int)

    private external fun setPan(channel: Int, pan: Float)

    private external fun setVolume(channel: Int, volume: Float)

    private external fun setPitchWheel(channel: Int, pitchWheel: Int)

    private external fun getPitchWheel(number: Int): Int

    private external fun getPitchRange(number: Int): Float

    private external fun getTuning(number: Int): Float

    private external fun setMidiControl(number: Int, control: Int, controlValue: Int)

    private external fun setPitchRange(number: Int, pitchRange: Float)

    private external fun setTuning(number: Int, tuning: Float)

    private external fun noteOn(number: Int, key: Int, velocity: Float)

    private external fun noteOff(number: Int, key: Int)

    private external fun noteOffAll(number: Int)

    private external fun soundsOffAll(number: Int)

    private external fun getPresetIndex(number: Int): Int

    private external fun getPresetBank(number: Int): Int

    private external fun getPresetNumber(number: Int): Int

    private external fun getPan(number: Int): Float

    private external fun getVolume(number: Int): Float
}