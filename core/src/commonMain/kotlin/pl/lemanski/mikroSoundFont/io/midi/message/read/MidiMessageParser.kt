package pl.lemanski.mikroSoundFont.io.midi.message.read

import pl.lemanski.mikroSoundFont.midi.MidiMessage

/**
 * Parser for midi messages.
 */
internal interface MidiMessageParser {
    /**
     * Parses a midi message.
     * @param deltaTime the delta time since the last message
     *
     * @return [MidiMessage]
     */
    fun parse(status: Int, deltaTime: Int = 0): MidiMessage

    /**
     * @return the types of midi messages that this parser supports.
     */
    fun supportedTypes(): Set<Int>
}