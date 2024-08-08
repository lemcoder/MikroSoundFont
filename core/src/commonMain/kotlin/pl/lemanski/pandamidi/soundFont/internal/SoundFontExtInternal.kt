package pl.lemanski.pandamidi.soundFont.internal

import pl.lemanski.pandamidi.soundFont.SoundFont

internal expect fun close(delegate: SoundFontDelegate)

internal expect fun reset(delegate: SoundFontDelegate)

internal expect fun getPresetIndex(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): Int

internal expect fun getPresetsCount(delegate: SoundFontDelegate): Int

internal expect fun getPresetName(delegate: SoundFontDelegate, presetIndex: Int): String

internal expect fun bankGetPresetName(delegate: SoundFontDelegate, bank: Int, presetNumber: Int): String

internal expect fun setOutput(delegate: SoundFontDelegate, outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float)

internal expect fun setVolume(delegate: SoundFontDelegate, globalGain: Float)

internal expect fun setMaxVoices(delegate: SoundFontDelegate, maxVoices: Int)

internal expect fun noteOn(delegate: SoundFontDelegate, presetIndex: Int, key: Int, velocity: Float)

internal expect fun bankNoteOn(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int, velocity: Float)

internal expect fun noteOff(delegate: SoundFontDelegate, presetIndex: Int, key: Int)

internal expect fun bankNoteOff(delegate: SoundFontDelegate, bank: Int, presetNumber: Int, key: Int)

internal expect fun noteOffAll(delegate: SoundFontDelegate)

internal expect fun activeVoiceCount(delegate: SoundFontDelegate): Int