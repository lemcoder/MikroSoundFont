package io.github.lemcoder.mikrosoundfont.internal

import io.github.lemcoder.mikrosoundfont.Channel
import io.github.lemcoder.mikrosoundfont.SoundFont

internal expect fun getChannelDelegate(
    number: Int,
    soundFont: SoundFont
): Channel