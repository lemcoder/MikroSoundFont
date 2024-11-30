package io.github.lemcoder.mikrosoundfont.io.midi.message

import kotlinx.io.Buffer
import kotlinx.io.readUByte
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException
import io.github.lemcoder.mikrosoundfont.io.midi.message.read.MidiMessageParser
import io.github.lemcoder.mikrosoundfont.io.midi.message.read.MidiMetaMessageParser
import io.github.lemcoder.mikrosoundfont.io.midi.message.read.MidiSystemMessageParser
import io.github.lemcoder.mikrosoundfont.io.midi.message.read.MidiVoiceMessageParser
import io.github.lemcoder.mikrosoundfont.io.midi.message.write.MidiMessageEncoder
import io.github.lemcoder.mikrosoundfont.io.midi.message.write.MidiMetaMessageEncoder
import io.github.lemcoder.mikrosoundfont.io.midi.message.write.MidiSystemMessageEncoder
import io.github.lemcoder.mikrosoundfont.io.midi.message.write.MidiVoiceMessageEncoder
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMetaMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiSystemMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import io.github.lemcoder.mikrosoundfont.midi.UnsupportedMidiMessage

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