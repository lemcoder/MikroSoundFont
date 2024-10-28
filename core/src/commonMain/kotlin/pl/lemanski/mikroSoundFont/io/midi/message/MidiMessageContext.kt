package pl.lemanski.mikroSoundFont.io.midi.message

import kotlinx.io.Buffer
import kotlinx.io.readUByte
import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMetaMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiSystemMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiVoiceMessageParser
import pl.lemanski.mikroSoundFont.midi.MidiMessage

internal class MidiMessageContext(
    private val buffer: Buffer
) {
    private val systemMessageParser = MidiSystemMessageParser(buffer)
    private val voiceMessageParser = MidiVoiceMessageParser(buffer)
    private val metaMessageParser = MidiMetaMessageParser(buffer)
    private lateinit var parser: MidiMessageParser
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

    fun writeMessage() {
        // TODO
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