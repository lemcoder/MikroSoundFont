package io.github.lemcoder.mikrosoundfont.internal

import io.github.lemcoder.mikrosoundfont.Channel
import io.github.lemcoder.mikrosoundfont.SoundFont

internal actual fun getSoundFontDelegate(path: String): SoundFont = SoundFontDelegate(path)

internal actual fun getSoundFontDelegate(memory: ByteArray): SoundFont = SoundFontDelegate(memory)

internal class SoundFontDelegate : SoundFont {
    override val channels: List<Channel>

    init {
        System.loadLibrary("mikroSoundFontJNI")  // Load native library
    }

    constructor(path: String) {
        if (!loadFilename(path)) throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    constructor(memory: ByteArray) {
        if (!loadMemory(memory, memory.size)) throw IllegalStateException("SoundFont not loaded")
        this.channels = (0..getPresetsCount()).map { getChannelDelegate(it, this) }
    }

    private external fun loadMemory(memory: ByteArray, size: Int): Boolean
    private external fun loadFilename(path: String): Boolean

    external override fun reset()
    external override fun getPresetIndex(bank: Int, presetNumber: Int): Int
    external override fun getPresetsCount(): Int
    external override fun getPresetName(presetIndex: Int): String
    external override fun bankGetPresetName(bank: Int, presetNumber: Int): String
    external override fun setOutput(
        outputMode: SoundFont.OutputMode,
        sampleRate: Int,
        globalGainDb: Float
    )

    external override fun setVolume(globalGain: Float)
    external override fun setMaxVoices(maxVoices: Int)
    external override fun noteOn(presetIndex: Int, key: Int, velocity: Float)
    external override fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float)
    external override fun noteOff(presetIndex: Int, key: Int)
    external override fun bankNoteOff(bank: Int, presetNumber: Int, key: Int)
    external override fun noteOffAll()
    external override fun activeVoiceCount(): Int
    external override fun setBankPreset(channel: Int, bank: Int, presetNumber: Int)
    external override fun renderFloat(samples: Int, channels: Int, isMixing: Boolean): FloatArray
}
