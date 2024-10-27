package pl.lemanski.mikroSoundFont.io.midi.message.read

import kotlinx.io.Buffer
import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessageType
import pl.lemanski.mikroSoundFont.midi.MidiMetaMessage
import pl.lemanski.mikroSoundFont.midi.UnsupportedMidiMessage

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

        return messageParserMap[metaType]
            ?.let { it(deltaTime) }
            ?: UnsupportedMidiMessage
    }

    private fun parseEndOfTrack(deltaTime: Int): MidiMetaMessage.EndOfTrack = MidiMetaMessage.EndOfTrack(
        time = deltaTime
    )

    private fun parseSetTempo(deltaTime: Int): MidiMetaMessage.SetTempo {
        val tempoBytes = buffer.readByte().toInt()
        if (tempoBytes != 0x03) {
            throw InvalidMidiDataException("Malformed MIDI. Set Tempo message must be 3 bytes long.")
        }

        var tempoValue = 0
        repeat(tempoBytes) {
            buffer.readByte()
            tempoValue = tempoValue shl 8 or buffer.readByte().toInt()
        }

        val bpm = 60_000_000 / tempoValue // 1 minute in microseconds / tempoValue

        return MidiMetaMessage.SetTempo(
            time = deltaTime,
            bpm = bpm
        )
    }
}