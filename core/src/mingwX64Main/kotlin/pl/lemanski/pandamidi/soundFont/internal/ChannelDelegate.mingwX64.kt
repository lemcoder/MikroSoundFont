package pl.lemanski.pandamidi.soundFont.internal

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import pl.lemanski.pandamidi.soundFont.Channel
import tinySoundFont.tsf_channel_get_pan
import tinySoundFont.tsf_channel_get_pitchrange
import tinySoundFont.tsf_channel_get_pitchwheel
import tinySoundFont.tsf_channel_get_preset_bank
import tinySoundFont.tsf_channel_get_preset_index
import tinySoundFont.tsf_channel_get_preset_number
import tinySoundFont.tsf_channel_get_tuning
import tinySoundFont.tsf_channel_get_volume
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
import tinySoundFont.tsf_channel_set_tuning
import tinySoundFont.tsf_channel_set_volume
import tinySoundFont.tsf_channel_sounds_off_all

@OptIn(ExperimentalForeignApi::class)
internal actual class ChannelDelegate(
    override val number: Int,
    private val soundFontDelegate: SoundFontDelegate
) : Channel {

    override fun setPresetIndex(presetIndex: Int) {
        withSoundFont {
            tsf_channel_set_presetindex(it.reinterpret(), number, presetIndex)
        }
    }

    override fun setPresetNumber(presetNumber: Int, isMidiDrums: Boolean) {
        withSoundFont {
            tsf_channel_set_presetnumber(it.reinterpret(), number, presetNumber, if (isMidiDrums) 1 else 0)
        }
    }

    override fun setBank(bank: Int) {
        withSoundFont {
            tsf_channel_set_bank(it.reinterpret(), number, bank)
        }
    }

    override fun setBankPreset(bank: Int, presetNumber: Int) {
        withSoundFont {
            tsf_channel_set_bank_preset(it.reinterpret(), number, bank, presetNumber)
        }
    }

    override fun setPan(pan: Float) {
        withSoundFont {
            tsf_channel_set_pan(it.reinterpret(), number, pan)
        }
    }

    override fun setVolume(volume: Float) {
        withSoundFont {
            tsf_channel_set_volume(it.reinterpret(), number, volume)
        }
    }

    override fun setPitchWheel(pitchWheel: Int) {
        withSoundFont {
            tsf_channel_set_pitchwheel(it.reinterpret(), number, pitchWheel)
        }
    }

    override fun setPitchRange(pitchRange: Float) {
        withSoundFont {
            tsf_channel_set_pitchrange(it.reinterpret(), number, pitchRange)
        }
    }

    override fun setTuning(tuning: Float) {
        withSoundFont {
            tsf_channel_set_tuning(it.reinterpret(), number, tuning)
        }
    }

    override fun noteOn(key: Int, velocity: Float) {
        withSoundFont {
            tsf_channel_note_on(it.reinterpret(), number, key, velocity)
        }
    }

    override fun noteOff(key: Int) {
        withSoundFont {
            tsf_channel_note_off(it.reinterpret(), number, key)
        }
    }

    override fun noteOffAll() {
        withSoundFont {
            tsf_channel_note_off_all(it.reinterpret(), number)
        }
    }

    override fun soundsOffAll() {
        withSoundFont {
            tsf_channel_sounds_off_all(it.reinterpret(), number)
        }
    }

    override fun getPresetIndex(): Int {
        return withSoundFont {
            tsf_channel_get_preset_index(it.reinterpret(), number)
        }
    }

    override fun getPresetBank(): Int {
        return withSoundFont {
            tsf_channel_get_preset_bank(it.reinterpret(), number)
        }
    }

    override fun getPresetNumber(): Int {
        return withSoundFont {
            tsf_channel_get_preset_number(it.reinterpret(), number)
        }
    }

    override fun getPan(): Float {
        return withSoundFont {
            tsf_channel_get_pan(it.reinterpret(), number)
        }
    }

    override fun getVolume(): Float {
        return withSoundFont {
            tsf_channel_get_volume(it.reinterpret(), number)
        }
    }

    override fun getPitchWheel(): Int {
        return withSoundFont {
            tsf_channel_get_pitchwheel(it.reinterpret(), number)
        }
    }

    override fun getPitchRange(): Float {
        return withSoundFont {
            tsf_channel_get_pitchrange(it.reinterpret(), number)
        }
    }

    override fun getTuning(): Float {
        return withSoundFont {
            tsf_channel_get_tuning(it.reinterpret(), number)
        }
    }

    private fun <T> withSoundFont(block: (soundFont: CPointer<*>) -> T): T = soundFontDelegate.soundFont.let(block) ?: throw IllegalStateException("SoundFont not loaded")
}