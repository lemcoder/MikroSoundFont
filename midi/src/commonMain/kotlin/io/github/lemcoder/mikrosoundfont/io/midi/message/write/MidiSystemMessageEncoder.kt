package io.github.lemcoder.mikrosoundfont.io.midi.message.write

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMessageType
import io.github.lemcoder.mikrosoundfont.midi.MidiSystemMessage

internal class MidiSystemMessageEncoder : MidiMessageEncoder {
    override fun encode(message: MidiMessage): ByteArray {
        return when (message) {
            is MidiSystemMessage.Sysex -> encodeSysex()
            is MidiSystemMessage.Eox   -> encodeEox()
            else                       -> byteArrayOf() // skip unsupported messages
        }
    }

    private fun encodeSysex(): ByteArray {
        val bytes = mutableListOf<Byte>()
        bytes.add(MidiMessageType.SYS_EX.value.toByte())
        // Typically, SysEx messages contain data and an end byte, but for now, we'll keep it simple
        bytes.add(0x00) // Placeholder for the data size (or more complex data)
        return bytes.toByteArray()
    }

    private fun encodeEox(): ByteArray {
        val bytes = mutableListOf<Byte>()
        bytes.add(MidiMessageType.EOX.value.toByte()) // EOX status byte
        // No additional data for EOX
        return bytes.toByteArray()
    }
}