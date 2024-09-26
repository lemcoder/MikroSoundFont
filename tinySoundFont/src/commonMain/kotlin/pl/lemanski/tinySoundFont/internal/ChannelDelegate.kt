package pl.lemanski.tinySoundFont.internal

import pl.lemanski.tinySoundFont.Channel
import pl.lemanski.tinySoundFont.SoundFont

internal expect fun getChannelDelegate(
    number: Int,
    soundFont: SoundFont
): Channel