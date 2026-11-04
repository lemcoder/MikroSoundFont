package io.github.lemcoder.mikrosoundfont.internal

import io.github.lemcoder.mikrosoundfont.Channel
import io.github.lemcoder.mikrosoundfont.SoundFont
import tinySoundFont.TsfPointer
import tinySoundFont.tsf_channel_get_pan
import tinySoundFont.tsf_channel_get_pitchrange
import tinySoundFont.tsf_channel_get_pitchwheel
import tinySoundFont.tsf_channel_get_preset_bank
import tinySoundFont.tsf_channel_get_preset_index
import tinySoundFont.tsf_channel_get_preset_number
import tinySoundFont.tsf_channel_get_tuning
import tinySoundFont.tsf_channel_get_volume
import tinySoundFont.tsf_channel_midi_control
import tinySoundFont.tsf_channel_note_off
import tinySoundFont.tsf_channel_note_off_all
import tinySoundFont.tsf_channel_note_on
import tinySoundFont.tsf_channel_set_bank
import tinySoundFont.tsf_channel_set_bank_preset
import tinySoundFont.tsf_channel_set_pan
import tinySoundFont.tsf_channel_set_pitchrange
import tinySoundFont.tsf_channel_set_pitchwheel
import tinySoundFont.tsf_channel_set_presetindex
import tinySoundFont.tsf_channel_set_presetnumber
import tinySoundFont.tsf_channel_set_sustain
import tinySoundFont.tsf_channel_set_tuning
import tinySoundFont.tsf_channel_set_volume
import tinySoundFont.tsf_channel_sounds_off_all

internal actual fun getChannelDelegate(
    number: Int,
    soundFont: SoundFont
): Channel = ChannelDelegate(number, soundFont as SoundFontDelegate)

internal class ChannelDelegate(
    override val number: Int,
    private val owner: SoundFontDelegate,
) : Channel {
    /** The owner's native `tsf*`, read per call so that a closed SoundFont throws. */
    private val soundFont: TsfPointer get() = owner.soundFont

    override fun setPresetIndex(presetIndex: Int) {
        tsf_channel_set_presetindex(soundFont, number, presetIndex)
    }

    override fun setPresetNumber(presetNumber: Int, isMidiDrums: Boolean) {
        tsf_channel_set_presetnumber(soundFont, number, presetNumber, if (isMidiDrums) 1 else 0)
    }

    override fun setBank(bank: Int) {
        tsf_channel_set_bank(soundFont, number, bank)
    }

    override fun setBankPreset(bank: Int, presetNumber: Int) {
        tsf_channel_set_bank_preset(soundFont, number, bank, presetNumber)
    }

    override fun setPan(pan: Float) {
        tsf_channel_set_pan(soundFont, number, pan)
    }

    override fun setVolume(volume: Float) {
        tsf_channel_set_volume(soundFont, number, volume)
    }

    override fun setPitchWheel(pitchWheel: Int) {
        tsf_channel_set_pitchwheel(soundFont, number, pitchWheel)
    }

    override fun setPitchRange(pitchRange: Float) {
        tsf_channel_set_pitchrange(soundFont, number, pitchRange)
    }

    override fun setTuning(tuning: Float) {
        tsf_channel_set_tuning(soundFont, number, tuning)
    }

    override fun setSustain(isSustained: Boolean) {
        tsf_channel_set_sustain(soundFont, number, if (isSustained) 1 else 0)
    }

    override fun noteOn(key: Int, velocity: Float) {
        tsf_channel_note_on(soundFont, number, key, velocity)
    }

    override fun noteOff(key: Int) = tsf_channel_note_off(soundFont, number, key)

    override fun noteOffAll() = tsf_channel_note_off_all(soundFont, number)

    override fun soundsOffAll() = tsf_channel_sounds_off_all(soundFont, number)

    override fun getPresetIndex(): Int = tsf_channel_get_preset_index(soundFont, number)

    override fun getPresetBank(): Int = tsf_channel_get_preset_bank(soundFont, number)

    override fun getPresetNumber(): Int = tsf_channel_get_preset_number(soundFont, number)

    override fun getPan(): Float = tsf_channel_get_pan(soundFont, number)

    override fun getVolume(): Float = tsf_channel_get_volume(soundFont, number)

    override fun getPitchWheel(): Int = tsf_channel_get_pitchwheel(soundFont, number)

    override fun getPitchRange(): Float = tsf_channel_get_pitchrange(soundFont, number)

    override fun getTuning(): Float = tsf_channel_get_tuning(soundFont, number)

    override fun setMidiControl(control: Int, controlValue: Int) {
        tsf_channel_midi_control(soundFont, number, control, controlValue)
    }
}
