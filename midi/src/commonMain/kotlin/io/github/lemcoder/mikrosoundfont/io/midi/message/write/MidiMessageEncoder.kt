package io.github.lemcoder.mikrosoundfont.io.midi.message.write

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage

interface MidiMessageEncoder {
    fun encode(message: MidiMessage): ByteArray
}