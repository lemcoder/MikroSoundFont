package io.github.lemcoder.mikrosoundfont.io.midi.message.read

import kotlinx.io.Buffer
import kotlinx.io.readUByte
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException
import io.github.lemcoder.mikrosoundfont.io.midi.message.readVarLen
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMessageType
import io.github.lemcoder.mikrosoundfont.midi.MidiMetaMessage
import io.github.lemcoder.mikrosoundfont.midi.UnsupportedMidiMessage

internal class MidiMetaMessageParser(
    private val buffer: Buffer
) : MidiMessageParser {
    private val metaMessageStatus = 0xFF // Meta messages always have status 0xFF.
    private val messageParserMap = mapOf(
        MidiMessageType.END_OF_TRACK.value to ::parseEndOfTrack,
        MidiMessageType.SET_TEMPO.value to ::parseSetTempo,
    )

    override fun supportedTypes(): Set<Int> = setOf(metaMessageStatus)

    override fun parse(status: Int, deltaTime: Int): MidiMessage {
        if (status != metaMessageStatus) {
            throw InvalidMidiDataException("Malformed MIDI. Meta messages must start with 0xFF.")
        }

        val metaType = buffer.readByte().toInt()
        val dataSize = buffer.readVarLen()

        return messageParserMap[metaType]
            ?.let { it(deltaTime, dataSize) }
            ?: skipMessage(dataSize)
    }

    private fun parseEndOfTrack(deltaTime: Int, dataSize: Int): MidiMetaMessage.EndOfTrack {
        if (dataSize != 0) {
            throw InvalidMidiDataException("Malformed MIDI. End Of Track message must be empty.")
        }

        return MidiMetaMessage.EndOfTrack(
            time = deltaTime
        )
    }

    private fun parseSetTempo(deltaTime: Int, dataSize: Int): MidiMetaMessage.SetTempo {
        if (dataSize != 3) {
            throw InvalidMidiDataException("Malformed MIDI. Set Tempo message must be 3 bytes long.")
        }

        var tempoValue = 0
        repeat(dataSize) {
            tempoValue = tempoValue shl 8 or buffer.readUByte().toInt()
        }

        return MidiMetaMessage.SetTempo(
            time = deltaTime,
            tempo = tempoValue
        )
    }

    private fun skipMessage(dataSize: Int): MidiMessage {
        buffer.skip(dataSize.toLong())
        return UnsupportedMidiMessage
    }
}