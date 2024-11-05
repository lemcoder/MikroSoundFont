package pl.lemanski.mikroSoundFont.io.midi.message

import kotlinx.io.Buffer
import kotlinx.io.readUByte
import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMetaMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiSystemMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiVoiceMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.write.MidiMessageEncoder
import pl.lemanski.mikroSoundFont.io.midi.message.write.MidiMetaMessageEncoder
import pl.lemanski.mikroSoundFont.io.midi.message.write.MidiSystemMessageEncoder
import pl.lemanski.mikroSoundFont.io.midi.message.write.MidiVoiceMessageEncoder
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMetaMessage
import pl.lemanski.mikroSoundFont.midi.MidiSystemMessage
import pl.lemanski.mikroSoundFont.midi.MidiVoiceMessage
import pl.lemanski.mikroSoundFont.midi.UnsupportedMidiMessage

internal class MidiMessageContext(
    private val buffer: Buffer
) {
    private val systemMessageParser = MidiSystemMessageParser(buffer)
    private val voiceMessageParser = MidiVoiceMessageParser(buffer)
    private val metaMessageParser = MidiMetaMessageParser(buffer)
    private lateinit var parser: MidiMessageParser

    //
    private val systemMessageEncoder = MidiSystemMessageEncoder()
    private val voiceMessageEncoder = MidiVoiceMessageEncoder()
    private val metaMessageEncoder = MidiMetaMessageEncoder()
    private lateinit var encoder: MidiMessageEncoder
    private var lastStatus: Int = 0

    fun readMessage(): MidiMessage {
        val dt = buffer.readVarLen()
        val status: Int = getStatusByte()

        parser = when (status) {
            in systemMessageParser.supportedTypes() -> systemMessageParser
            in metaMessageParser.supportedTypes()   -> metaMessageParser
            else                                    -> voiceMessageParser
        }

        return parser.parse(status, dt)
    }

    fun writeMessage(message: MidiMessage) {
        buffer.writeVarLen(message.time)

        encoder = when (message) {
            is MidiMetaMessage        -> metaMessageEncoder
            is MidiSystemMessage      -> systemMessageEncoder
            is MidiVoiceMessage       -> voiceMessageEncoder
            is UnsupportedMidiMessage -> return
        }

        buffer.write(encoder.encode(message))
    }

    //---

    private fun getStatusByte(): Int {
        val tmp = buffer.peek().readUByte().toInt() // do not consume, might have running status

        return if (tmp.isValidStatus()) {
            val statusByte = buffer.readUByte().toInt()
            lastStatus = statusByte
            statusByte
        } else {
            if (!lastStatus.isValidStatus()) {
                throw InvalidMidiDataException("Invalid status: ${tmp.toString(16)} and no running status detected")
            }
            lastStatus
        }
    }

    private fun Int.isValidStatus(): Boolean = this and 0x80 != 0
}