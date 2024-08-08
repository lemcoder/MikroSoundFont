package pl.lemanski.pandamidi.soundFont.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import pl.lemanski.pandamidi.soundFont.SoundFont
import tinySoundFont.TSFOutputMode
import tinySoundFont.tsf_active_voice_count
import tinySoundFont.tsf_bank_get_presetname
import tinySoundFont.tsf_bank_note_off
import tinySoundFont.tsf_bank_note_on
import tinySoundFont.tsf_close
import tinySoundFont.tsf_get_presetcount
import tinySoundFont.tsf_get_presetindex
import tinySoundFont.tsf_get_presetname
import tinySoundFont.tsf_note_off
import tinySoundFont.tsf_note_off_all
import tinySoundFont.tsf_note_on
import tinySoundFont.tsf_reset
import tinySoundFont.tsf_set_max_voices
import tinySoundFont.tsf_set_output
import tinySoundFont.tsf_set_volume

@OptIn(ExperimentalForeignApi::class)
internal actual fun close(delegate: SoundFontDelegate) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_close(ref.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun reset(delegate: SoundFontDelegate) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_reset(ref.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetIndex(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): Int {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        return tsf_get_presetindex(ref.reinterpret(), bank, presetNumber)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetsCount(delegate: SoundFontDelegate): Int {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        return tsf_get_presetcount(ref.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPresetName(delegate: SoundFontDelegate, presetIndex: Int): String {
    memScoped {
        val ref = StableRef.create(delegate.sf)
        val buffer = tsf_get_presetname(ref.asCPointer().reinterpret(), presetIndex) ?: return ""
        return buffer.toKString()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankGetPresetName(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): String {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        val buffer = tsf_bank_get_presetname(ref.reinterpret(), bank, presetNumber) ?: return ""
        return buffer.toKString()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setOutput(delegate: SoundFontDelegate, outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        val tsfMode = when (outputMode) {
            SoundFont.OutputMode.TSF_STEREO_INTERLEAVED -> TSFOutputMode.TSF_STEREO_INTERLEAVED
            SoundFont.OutputMode.TSF_STEREO_UNWEAVED    -> TSFOutputMode.TSF_STEREO_UNWEAVED
            SoundFont.OutputMode.TSF_MONO               -> TSFOutputMode.TSF_MONO
        }
        tsf_set_output(ref.reinterpret(), tsfMode, sampleRate, globalGainDb)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setVolume(delegate: SoundFontDelegate, globalGain: Float) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_set_volume(ref.reinterpret(), globalGain)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setMaxVoices(delegate: SoundFontDelegate, maxVoices: Int) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_set_max_voices(ref.reinterpret(), maxVoices)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOn(delegate: SoundFontDelegate, presetIndex: Int, key: Int, velocity: Float) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_note_on(ref.reinterpret(), presetIndex, key, velocity)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankNoteOn(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int, velocity: Float) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_bank_note_on(ref.reinterpret(), bank, presetNumber, key, velocity)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOff(delegate: SoundFontDelegate, presetIndex: Int, key: Int) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_note_off(ref.reinterpret(), presetIndex, key)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun bankNoteOff(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_bank_note_off(ref.reinterpret(), bank, presetNumber, key)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun noteOffAll(delegate: SoundFontDelegate) {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        tsf_note_off_all(ref.reinterpret())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun activeVoiceCount(delegate: SoundFontDelegate): Int {
    memScoped {
        val ref = StableRef.create(delegate.sf).asCPointer()
        return tsf_active_voice_count(ref.reinterpret())
    }
}