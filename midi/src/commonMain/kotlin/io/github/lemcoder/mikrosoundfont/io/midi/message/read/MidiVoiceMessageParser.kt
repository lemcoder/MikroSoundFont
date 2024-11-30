package io.github.lemcoder.mikrosoundfont.io.midi.message.read

import kotlinx.io.Buffer
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMessageType
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage

internal class MidiVoiceMessageParser(
    private val buffer: Buffer
) : MidiMessageParser {
    private val messageParserMap = mapOf(
        MidiMessageType.NOTE_OFF.value to ::parseNoteOff,
        MidiMessageType.NOTE_ON.value to ::parseNoteOn,
        MidiMessageType.KEY_PRESSURE.value to ::parseKeyPressure,
        MidiMessageType.CONTROL_CHANGE.value to ::parseControlChange,
        MidiMessageType.PROGRAM_CHANGE.value to ::parseProgramChange,
        MidiMessageType.CHANNEL_PRESSURE.value to ::parseChannelPressure,
        MidiMessageType.PITCH_BEND.value to ::parsePitchBend
    )

    override fun supportedTypes(): Set<Int> = messageParserMap.keys

    override fun parse(status: Int, deltaTime: Int): MidiMessage = messageParserMap[status and 0xf0]
        ?.let { it(status, deltaTime) }
        ?: throw InvalidMidiDataException("Unknown midi voice message type: ${(status and 0xf0).toString(16)}")

    private fun parseNoteOff(status: Int, deltaTime: Int): MidiVoiceMessage.NoteOff {
        val channel = status and 0x0f
        val note = buffer.readByte().toInt()
        val velocity = buffer.readByte().toInt()

        return MidiVoiceMessage.NoteOff(
            time = deltaTime,
            channel = channel,
            key = note,
            velocity = velocity
        )
    }

    private fun parseNoteOn(status: Int, deltaTime: Int): MidiVoiceMessage.NoteOn {
        val channel = status and 0x0f
        val note = buffer.readByte().toInt()
        val velocity = buffer.readByte().toInt()

        return MidiVoiceMessage.NoteOn(
            time = deltaTime,
            channel = channel,
            key = note,
            velocity = velocity
        )
    }

    private fun parseKeyPressure(status: Int, deltaTime: Int): MidiVoiceMessage.KeyPressure {
        val channel = status and 0x0f
        val note = buffer.readByte().toInt()
        val keyPressure = buffer.readByte().toInt()

        return MidiVoiceMessage.KeyPressure(
            time = deltaTime,
            channel = channel,
            key = note,
            keyPressure = keyPressure
        )
    }

    private fun parseControlChange(status: Int, deltaTime: Int): MidiVoiceMessage.ControlChange {
        val channel = status and 0x0f
        val control = buffer.readByte().toInt()
        val controlValue = buffer.readByte().toInt()

        return MidiVoiceMessage.ControlChange(
            time = deltaTime,
            channel = channel,
            control = control,
            controlValue = controlValue
        )
    }

    private fun parseProgramChange(status: Int, deltaTime: Int): MidiVoiceMessage.ProgramChange {
        val channel = status and 0x0f
        val program = buffer.readByte().toInt()

        return MidiVoiceMessage.ProgramChange(
            time = deltaTime,
            channel = channel,
            program = program
        )
    }

    private fun parseChannelPressure(status: Int, deltaTime: Int): MidiVoiceMessage.ChannelPressure {
        val channel = status and 0x0f
        val channelPressure = buffer.readByte().toInt()

        return MidiVoiceMessage.ChannelPressure(
            time = deltaTime,
            channel = channel,
            channelPressure = channelPressure
        )
    }

    private fun parsePitchBend(status: Int, deltaTime: Int): MidiVoiceMessage.PitchBend {
        val channel = status and 0x0f
        val lsb = buffer.readByte().toInt() and 0x7F
        val msb = buffer.readByte().toInt() and 0x7F
        val pitchBend = (msb shl 7) or lsb

        return MidiVoiceMessage.PitchBend(
            time = deltaTime,
            channel = channel,
            pitchBend = pitchBend
        )
    }
}
