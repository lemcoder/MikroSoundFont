package pl.lemanski.mikroSoundFont.io.midi.message.write

import pl.lemanski.mikroSoundFont.midi.MidiMessage

interface MidiMessageEncoder {
    fun encode(message: MidiMessage): ByteArray
}