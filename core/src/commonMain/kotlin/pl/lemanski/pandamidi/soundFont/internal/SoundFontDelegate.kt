package pl.lemanski.pandamidi.soundFont.internal

import pl.lemanski.pandamidi.soundFont.SoundFont

internal open class SoundFontDelegate : SoundFont {

    override fun copy(): SoundFont = SoundFontDelegate() // TODO implement

    override fun close() = close(this)

    override fun reset() = reset(this)

    override fun getPresetIndex(bank: Int, presetNumber: Int): Int = getPresetIndex(this, bank, presetNumber)

    override fun getPresetsCount(): Int = getPresetsCount(this)

    override fun getPresetName(presetIndex: Int): String = getPresetName(this, presetIndex)

    override fun bankGetPresetName(bank: Int, presetNumber: Int): String = bankGetPresetName(this, bank, presetNumber)

    override fun setOutput(outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) = setOutput(this, outputMode, sampleRate, globalGainDb)

    override fun setVolume(globalGain: Float) = setVolume(this, globalGain)

    override fun setMaxVoices(maxVoices: Int) = setMaxVoices(this, maxVoices)

    override fun noteOn(presetIndex: Int, key: Int, velocity: Float) = noteOn(this, presetIndex, key, velocity)

    override fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float) = bankNoteOn(this, bank, presetNumber, key, velocity)

    override fun noteOff(presetIndex: Int, key: Int) = noteOff(this, presetIndex, key)

    override fun bankNoteOff(bank: Int, presetNumber: Int, key: Int) = bankNoteOff(this, bank, presetNumber, key)

    override fun noteOffAll() = noteOffAll(this)

    override fun activeVoiceCount(): Int = activeVoiceCount(this)

    override fun setBankPreset(channel: Int, bank: Int, presetNumber: Int) = setBankPresetNumber(this, channel, bank, presetNumber)

    override fun renderFloat(samples: Int, isMixing: Boolean): FloatArray = renderFloat(this, samples, isMixing)
}