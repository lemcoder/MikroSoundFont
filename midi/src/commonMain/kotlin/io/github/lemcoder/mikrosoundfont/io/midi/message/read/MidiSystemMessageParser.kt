package io.github.lemcoder.mikrosoundfont.io.midi.message.read

import kotlinx.io.Buffer
import io.github.lemcoder.mikrosoundfont.io.midi.message.readVarLen
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMessageType
import io.github.lemcoder.mikrosoundfont.midi.MidiSystemMessage
import io.github.lemcoder.mikrosoundfont.midi.UnsupportedMidiMessage

/**
 * Not supported yet
 */
class MidiSystemMessageParser(
    private val buffer: Buffer
) : MidiMessageParser {
    private val messageParserMap = mapOf(
        MidiMessageType.SYS_EX.value to ::parseSysex,
        MidiMessageType.EOX.value to ::parseEox,
    )

    override fun supportedTypes(): Set<Int> = messageParserMap.keys

    override fun parse(status: Int, deltaTime: Int): MidiMessage = messageParserMap[status]
        ?.let { it() }
        ?: UnsupportedMidiMessage

    // Not supported yet
    private fun parseEox(): MidiSystemMessage.Eox {
        buffer.readVarLen()
        return MidiSystemMessage.Eox
    }

    // Not supported yet
    private fun parseSysex(): MidiSystemMessage.Sysex {
        buffer.readVarLen()
        return MidiSystemMessage.Sysex
    }
}