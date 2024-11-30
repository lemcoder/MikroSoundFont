package io.github.lemcoder.mikrosoundfont.internal

import io.github.lemcoder.mikrosoundfont.SoundFont

internal expect fun getSoundFontDelegate(path: String): SoundFont

internal expect fun getSoundFontDelegate(memory: ByteArray): SoundFont