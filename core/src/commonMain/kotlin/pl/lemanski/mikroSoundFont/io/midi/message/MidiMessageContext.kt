package pl.lemanski.mikroSoundFont.io.midi.message

import kotlinx.io.Buffer
import kotlinx.io.readUByte
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiMetaMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiSystemMessageParser
import pl.lemanski.mikroSoundFont.io.midi.message.read.MidiVoiceMessageParser
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.UnsupportedMidiMessage

internal class MidiMessageContext(
    private val buffer: Buffer
) {
    private val systemMessageParser = MidiSystemMessageParser(buffer)
    private val voiceMessageParser = MidiVoiceMessageParser(buffer)
    private val metaMessageParser = MidiMetaMessageParser(buffer)
    private lateinit var parser: MidiMessageParser

    fun readMessage(): MidiMessage {
        val dt = buffer.readVarLen()
        val status = buffer.readUByte().toInt()

        parser = when (status) {
            in systemMessageParser.supportedTypes() -> systemMessageParser
            in metaMessageParser.supportedTypes()   -> metaMessageParser
            in voiceMessageParser.supportedTypes()  -> voiceMessageParser
            else                                    -> return UnsupportedMidiMessage
        }

        return parser.parse(status, dt)
    }

    fun writeMessage() {
        // TODO
    }
}