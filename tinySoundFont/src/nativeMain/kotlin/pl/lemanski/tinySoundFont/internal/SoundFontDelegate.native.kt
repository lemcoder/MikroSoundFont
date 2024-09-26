package pl.lemanski.tinySoundFont.internal

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.toKString
import pl.lemanski.tinySoundFont.Channel
import pl.lemanski.tinySoundFont.SoundFont
import tinySoundFont.TSFOutputMode
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

@OptIn(ExperimentalForeignApi::class)
internal class SoundFontDelegate : SoundFont {
    internal val soundFont: CPointer<*>
    override val channels: List<Channel>

    constructor(path: String) {
        this.soundFont =
            tsf_load_filename(path) ?: throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    constructor(memory: ByteArray) {
        this.soundFont = tsf_load_memory(memory.toCValues(), memory.size)
            ?: throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    override fun reset() {
        withSoundFont {
            tsf_reset(it.reinterpret())
        }
    }

    override fun getPresetIndex(bank: Int, presetNumber: Int): Int {
        return withSoundFont {
            tsf_get_presetindex(it.reinterpret(), bank, presetNumber)
        }
    }

    override fun getPresetsCount(): Int {
        return withSoundFont {
            tsf_get_presetcount(it.reinterpret())
        }
    }

    override fun getPresetName(presetIndex: Int): String {
        return withSoundFont {
            val buffer =
                tsf_get_presetname(it.reinterpret(), presetIndex) ?: return@withSoundFont ""
            buffer.toKString()
        }
    }

    override fun bankGetPresetName(bank: Int, presetNumber: Int): String {
        return withSoundFont {
            val buffer = tsf_bank_get_presetname(it.reinterpret(), bank, presetNumber)
                ?: return@withSoundFont ""
            buffer.toKString()
        }
    }

    override fun setOutput(outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) {
        return withSoundFont {
            val tsfMode = when (outputMode) {
                SoundFont.OutputMode.TSF_STEREO_INTERLEAVED -> TSFOutputMode.TSF_STEREO_INTERLEAVED
                SoundFont.OutputMode.TSF_STEREO_UNWEAVED -> TSFOutputMode.TSF_STEREO_UNWEAVED
                SoundFont.OutputMode.TSF_MONO -> TSFOutputMode.TSF_MONO
            }
            tsf_set_output(it.reinterpret(), tsfMode, sampleRate, globalGainDb)
        }
    }

    override fun setVolume(globalGain: Float) {
        withSoundFont {
            tsf_set_volume(it.reinterpret(), globalGain)
        }
    }

    override fun setMaxVoices(maxVoices: Int) {
        withSoundFont {
            tsf_set_max_voices(it.reinterpret(), maxVoices)
        }
    }

    override fun noteOn(presetIndex: Int, key: Int, velocity: Float) {
        withSoundFont {
            tsf_note_on(it.reinterpret(), presetIndex, key, velocity)
        }
    }

    override fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float) {
        withSoundFont {
            tsf_bank_note_on(it.reinterpret(), bank, presetNumber, key, velocity)
        }
    }

    override fun noteOff(presetIndex: Int, key: Int) {
        withSoundFont {
            tsf_note_off(it.reinterpret(), presetIndex, key)
        }
    }

    override fun bankNoteOff(bank: Int, presetNumber: Int, key: Int) {
        withSoundFont {
            tsf_bank_note_off(it.reinterpret(), bank, presetNumber, key)
        }
    }

    override fun noteOffAll() {
        withSoundFont {
            tsf_note_off_all(it.reinterpret())
        }
    }

    override fun activeVoiceCount(): Int {
        return withSoundFont {
            tsf_active_voice_count(it.reinterpret())
        }
    }

    override fun setBankPreset(channel: Int, bank: Int, presetNumber: Int) {
        withSoundFont {
            tsf_channel_set_bank_preset(it.reinterpret(), channel, bank, presetNumber)
        }
    }

    override fun renderFloat(samples: Int, isMixing: Boolean): FloatArray {
        return withSoundFont {
            val buffer = FloatArray(samples * 2)
            val flagMixing = if (isMixing) 1 else 0
            tsf_render_float(it.reinterpret(), buffer.refTo(0), samples, flagMixing)
            buffer
        }
    }

    private fun <T> withSoundFont(block: (soundFont: CPointer<*>) -> T): T =
        this.soundFont.let(block) ?: throw IllegalStateException("SoundFont not loaded")
}