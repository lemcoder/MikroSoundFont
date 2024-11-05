package pl.lemanski.mikroSoundFont.io.midi.message.write

import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessageType
import pl.lemanski.mikroSoundFont.midi.MidiSystemMessage

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