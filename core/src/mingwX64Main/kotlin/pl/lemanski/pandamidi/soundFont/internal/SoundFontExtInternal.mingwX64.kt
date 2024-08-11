package pl.lemanski.pandamidi.soundFont.internal

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import pl.lemanski.pandamidi.soundFont.SoundFont
import tinySoundFont.TSFOutputMode
import tinySoundFont.tsf_active_voice_count
import tinySoundFont.tsf_bank_get_presetname
import tinySoundFont.tsf_bank_note_off
import tinySoundFont.tsf_bank_note_on
import tinySoundFont.tsf_channel_set_bank_preset
import tinySoundFont.tsf_close
import tinySoundFont.tsf_get_presetcount
import tinySoundFont.tsf_get_presetindex
import tinySoundFont.tsf_get_presetname
import tinySoundFont.tsf_note_off
import tinySoundFont.tsf_note_off_all
import tinySoundFont.tsf_note_on
import tinySoundFont.tsf_render_float
import tinySoundFont.tsf_reset
import tinySoundFont.tsf_set_max_voices
import tinySoundFont.tsf_set_output
import tinySoundFont.tsf_set_volume

@OptIn(ExperimentalForeignApi::class)
internal actual fun close(delegate: SoundFontDelegate) {
    withSoundFont {
        tsf_close(it.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun reset(delegate: SoundFontDelegate) {
    withSoundFont {
        tsf_reset(it.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetIndex(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): Int {
    return withSoundFont {
        tsf_get_presetindex(it.reinterpret(), bank, presetNumber)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetsCount(delegate: SoundFontDelegate): Int {
    return withSoundFont {
        tsf_get_presetcount(it.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetName(delegate: SoundFontDelegate, presetIndex: Int): String {
    return withSoundFont {
        val buffer = tsf_get_presetname(it.reinterpret(), presetIndex) ?: return@withSoundFont ""
        buffer.toKString()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankGetPresetName(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): String {
    return withSoundFont {
        val buffer = tsf_bank_get_presetname(it.reinterpret(), bank, presetNumber) ?: return@withSoundFont ""
        buffer.toKString()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setOutput(delegate: SoundFontDelegate, outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) {
    return withSoundFont {
        val tsfMode = when (outputMode) {
            SoundFont.OutputMode.TSF_STEREO_INTERLEAVED -> TSFOutputMode.TSF_STEREO_INTERLEAVED
            SoundFont.OutputMode.TSF_STEREO_UNWEAVED    -> TSFOutputMode.TSF_STEREO_UNWEAVED
            SoundFont.OutputMode.TSF_MONO               -> TSFOutputMode.TSF_MONO
        }
        tsf_set_output(it.reinterpret(), tsfMode, sampleRate, globalGainDb)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setVolume(delegate: SoundFontDelegate, globalGain: Float) {
    withSoundFont {
        tsf_set_volume(it.reinterpret(), globalGain)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setMaxVoices(delegate: SoundFontDelegate, maxVoices: Int) {
    withSoundFont {
        tsf_set_max_voices(it.reinterpret(), maxVoices)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOn(delegate: SoundFontDelegate, presetIndex: Int, key: Int, velocity: Float) {
    withSoundFont {
        tsf_note_on(it.reinterpret(), presetIndex, key, velocity)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankNoteOn(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int, velocity: Float) {
    withSoundFont {
        tsf_bank_note_on(it.reinterpret(), bank, presetNumber, key, velocity)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOff(delegate: SoundFontDelegate, presetIndex: Int, key: Int) {
    withSoundFont {
        tsf_note_off(it.reinterpret(), presetIndex, key)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankNoteOff(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int) {
    withSoundFont {
        tsf_bank_note_off(it.reinterpret(), bank, presetNumber, key)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOffAll(delegate: SoundFontDelegate) {
    withSoundFont {
        tsf_note_off_all(it.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun activeVoiceCount(delegate: SoundFontDelegate): Int {
    return withSoundFont {
        tsf_active_voice_count(it.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setBankPresetNumber(delegate: SoundFontDelegate, channel: Int, bank: Int, presetNumber: Int) {
    withSoundFont {
        tsf_channel_set_bank_preset(it.reinterpret(), channel, bank, presetNumber)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun renderFloat(delegate: SoundFontDelegate, samples: Int, isMixing: Boolean): FloatArray {
    return withSoundFont {
        val buffer = FloatArray(samples * 2)
        val flagMixing = if (isMixing) 1 else 0
        tsf_render_float(it.reinterpret(), buffer.refTo(0), samples, flagMixing)
        buffer
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun <T> withSoundFont(block: (soundFont: CPointer<*>) -> T): T = SoundFontHolder.tsf?.let(block) ?: throw IllegalStateException("SoundFont not loaded")