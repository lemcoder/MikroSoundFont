package pl.lemanski.mikroSoundFont.internal

import pl.lemanski.mikroSoundFont.Channel
import pl.lemanski.mikroSoundFont.SoundFont

internal expect fun getChannelDelegate(
    number: Int,
    soundFont: SoundFont
): Channel