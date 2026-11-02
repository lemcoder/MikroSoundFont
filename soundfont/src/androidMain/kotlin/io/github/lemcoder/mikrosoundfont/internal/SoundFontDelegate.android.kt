package io.github.lemcoder.mikrosoundfont.internal

import io.github.lemcoder.mikrosoundfont.Channel
import io.github.lemcoder.mikrosoundfont.SoundFont
import java.nio.ByteBuffer
import tinySoundFont.TSF_MONO
import tinySoundFont.TSF_STEREO_INTERLEAVED
import tinySoundFont.TSF_STEREO_UNWEAVED
import tinySoundFont.tsf_active_voice_count
import tinySoundFont.tsf_bank_get_presetname
import tinySoundFont.tsf_bank_note_off
import tinySoundFont.tsf_bank_note_on
import tinySoundFont.tsf_channel_set_bank_preset
import tinySoundFont.tsf_get_presetcount
import tinySoundFont.tsf_get_presetindex
import tinySoundFont.tsf_get_presetname
import tinySoundFont.tsf_load_filename
import tinySoundFont.tsf_load_memory
import tinySoundFont.tsf_note_off
import tinySoundFont.tsf_note_off_all
import tinySoundFont.tsf_note_on
import tinySoundFont.tsf_render_float
import tinySoundFont.tsf_reset
import tinySoundFont.tsf_set_max_voices
import tinySoundFont.tsf_set_output
import tinySoundFont.tsf_set_volume

internal actual fun getSoundFontDelegate(path: String): SoundFont = SoundFontDelegate(path)

internal actual fun getSoundFontDelegate(memory: ByteArray): SoundFont = SoundFontDelegate(memory)

internal class SoundFontDelegate : SoundFont {
    /** The native `tsf*`. */
    internal val soundFont: Long
    override val channels: List<Channel>

    constructor(path: String) {
        this.soundFont = tsf_load_filename(path).takeIf { it != 0L }
            ?: throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    constructor(memory: ByteArray) {
        // tsf_load_memory takes a void*, which crosses JNI as a direct buffer; TinySoundFont copies what it needs.
        val buffer = ByteBuffer.allocateDirect(memory.size).put(memory)
        this.soundFont = tsf_load_memory(buffer, memory.size).takeIf { it != 0L }
            ?: throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    override fun reset() = tsf_reset(soundFont)

    override fun getPresetIndex(bank: Int, presetNumber: Int): Int =
        tsf_get_presetindex(soundFont, bank, presetNumber)

    override fun getPresetsCount(): Int = tsf_get_presetcount(soundFont)

    override fun getPresetName(presetIndex: Int): String =
        tsf_get_presetname(soundFont, presetIndex).orEmpty()

    override fun bankGetPresetName(bank: Int, presetNumber: Int): String =
        tsf_bank_get_presetname(soundFont, bank, presetNumber).orEmpty()

    override fun setOutput(outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) {
        val tsfMode = when (outputMode) {
            SoundFont.OutputMode.STEREO_INTERLEAVED -> TSF_STEREO_INTERLEAVED
            SoundFont.OutputMode.STEREO_UNWEAVED -> TSF_STEREO_UNWEAVED
            SoundFont.OutputMode.MONO -> TSF_MONO
        }
        tsf_set_output(soundFont, tsfMode, sampleRate, globalGainDb)
    }

    override fun setVolume(globalGain: Float) = tsf_set_volume(soundFont, globalGain)

    override fun setMaxVoices(maxVoices: Int) {
        tsf_set_max_voices(soundFont, maxVoices)
    }

    override fun noteOn(presetIndex: Int, key: Int, velocity: Float) {
        tsf_note_on(soundFont, presetIndex, key, velocity)
    }

    override fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float) {
        tsf_bank_note_on(soundFont, bank, presetNumber, key, velocity)
    }

    override fun noteOff(presetIndex: Int, key: Int) = tsf_note_off(soundFont, presetIndex, key)

    override fun bankNoteOff(bank: Int, presetNumber: Int, key: Int) {
        tsf_bank_note_off(soundFont, bank, presetNumber, key)
    }

    override fun noteOffAll() = tsf_note_off_all(soundFont)

    override fun activeVoiceCount(): Int = tsf_active_voice_count(soundFont)

    override fun setBankPreset(channel: Int, bank: Int, presetNumber: Int) {
        tsf_channel_set_bank_preset(soundFont, channel, bank, presetNumber)
    }

    override fun renderFloat(samples: Int, channels: Int, isMixing: Boolean): FloatArray {
        val buffer = FloatArray(samples * channels)
        tsf_render_float(soundFont, buffer, samples, if (isMixing) 1 else 0)
        return buffer
    }
}
