package pl.lemanski.mikroSoundFont.io.midi.message.write

import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessageType
import pl.lemanski.mikroSoundFont.midi.MidiMetaMessage

internal class MidiMetaMessageEncoder : MidiMessageEncoder {
    private val metaMessageStatus = 0xFF // Meta messages always have status 0xFF.

    override fun encode(message: MidiMessage): ByteArray {
        return when (message) {
            is MidiMetaMessage.EndOfTrack -> encodeEndOfTrack(message)
            is MidiMetaMessage.SetTempo   -> encodeSetTempo(message)
            else                          -> byteArrayOf() // skip unsupported messages
        }
    }

    private fun encodeEndOfTrack(message: MidiMetaMessage.EndOfTrack): ByteArray {
        val bytes = mutableListOf<Byte>()
        bytes.add(metaMessageStatus.toByte())
        bytes.add(MidiMessageType.END_OF_TRACK.value.toByte())
        bytes.add(0)

        return bytes.toByteArray()
    }

    private fun encodeSetTempo(message: MidiMetaMessage.SetTempo): ByteArray {
        val bytes = mutableListOf<Byte>()
        bytes.add(metaMessageStatus.toByte())
        bytes.add(MidiMessageType.SET_TEMPO.value.toByte())
        bytes.add(3)

        val tempo = message.tempo
        bytes.add((tempo shr 16 and 0xFF).toByte())
        bytes.add((tempo shr 8 and 0xFF).toByte())
        bytes.add((tempo and 0xFF).toByte())

        return bytes.toByteArray()
    }
}