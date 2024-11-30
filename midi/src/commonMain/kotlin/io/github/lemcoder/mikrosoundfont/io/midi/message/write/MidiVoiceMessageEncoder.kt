package io.github.lemcoder.mikrosoundfont.io.midi.message.write

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMessageType
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage

internal class MidiVoiceMessageEncoder : MidiMessageEncoder {
    override fun encode(message: MidiMessage): ByteArray {
        return when (message) {
            is MidiVoiceMessage.NoteOff         -> encodeNoteOff(message)
            is MidiVoiceMessage.NoteOn          -> encodeNoteOn(message)
            is MidiVoiceMessage.KeyPressure     -> encodeKeyPressure(message)
            is MidiVoiceMessage.ControlChange   -> encodeControlChange(message)
            is MidiVoiceMessage.ProgramChange   -> encodeProgramChange(message)
            is MidiVoiceMessage.ChannelPressure -> encodeChannelPressure(message)
            is MidiVoiceMessage.PitchBend       -> encodePitchBend(message)
            else                                -> byteArrayOf() // Unsupported messages
        }
    }

    private fun encodeNoteOff(message: MidiVoiceMessage.NoteOff): ByteArray {
        val status = MidiMessageType.NOTE_OFF.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.key.toByte(),
            message.velocity.toByte()
        )
    }

    private fun encodeNoteOn(message: MidiVoiceMessage.NoteOn): ByteArray {
        val status = MidiMessageType.NOTE_ON.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.key.toByte(),
            message.velocity.toByte()
        )
    }

    private fun encodeKeyPressure(message: MidiVoiceMessage.KeyPressure): ByteArray {
        val status = MidiMessageType.KEY_PRESSURE.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.key.toByte(),
            message.keyPressure.toByte()
        )
    }

    private fun encodeControlChange(message: MidiVoiceMessage.ControlChange): ByteArray {
        val status = MidiMessageType.CONTROL_CHANGE.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.control.toByte(),
            message.controlValue.toByte()
        )
    }

    private fun encodeProgramChange(message: MidiVoiceMessage.ProgramChange): ByteArray {
        val status = MidiMessageType.PROGRAM_CHANGE.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.program.toByte()
        )
    }

    private fun encodeChannelPressure(message: MidiVoiceMessage.ChannelPressure): ByteArray {
        val status = MidiMessageType.CHANNEL_PRESSURE.value or (message.channel and 0x0F)
        return byteArrayOf(
            status.toByte(),
            message.channelPressure.toByte()
        )
    }

    private fun encodePitchBend(message: MidiVoiceMessage.PitchBend): ByteArray {
        val status = MidiMessageType.PITCH_BEND.value or (message.channel and 0x0F)
        val lsb = (message.pitchBend and 0x7F).toByte() // Least significant 7 bits
        val msb = ((message.pitchBend shr 7) and 0x7F).toByte() // Most significant 7 bits
        return byteArrayOf(
            status.toByte(),
            lsb,
            msb
        )
    }
}