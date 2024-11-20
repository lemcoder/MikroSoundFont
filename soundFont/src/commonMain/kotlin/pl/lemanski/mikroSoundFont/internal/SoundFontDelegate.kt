package pl.lemanski.mikroSoundFont.internal

import pl.lemanski.mikroSoundFont.SoundFont

internal expect fun getSoundFontDelegate(path: String): SoundFont

internal expect fun getSoundFontDelegate(memory: ByteArray): SoundFont