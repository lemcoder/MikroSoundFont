package pl.lemanski.mikroSoundFont.io.midi.message.read

import kotlinx.io.Buffer
import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessageType
import pl.lemanski.mikroSoundFont.midi.MidiVoiceMessage

/**
 * else //channel message
 * 	{
 * 		int param;
 * 		if ((param = tml_readbyte(p)) < 0) { TML_WARN("Unexpected end of file"); return -1; }
 * 		evt->key = (param & 0x7f);
 * 		evt->channel = (status & 0x0f);
 * 		switch (evt->type = (status & 0xf0))
 * 		{
 * 			case TML_NOTE_OFF:
 * 			case TML_NOTE_ON:
 * 			case TML_KEY_PRESSURE:
 * 			case TML_CONTROL_CHANGE:
 * 				if ((param = tml_readbyte(p)) < 0) { TML_WARN("Unexpected end of file"); return -1; }
 * 				evt->velocity = (param & 0x7f);
 * 				break;
 *
 * 			case TML_PITCH_BEND:
 * 				if ((param = tml_readbyte(p)) < 0) { TML_WARN("Unexpected end of file"); return -1; }
 * 				evt->pitch_bend = ((param & 0x7f) << 7) | evt->key;
 * 				break;
 *
 * 			case TML_PROGRAM_CHANGE:
 * 			case TML_CHANNEL_PRESSURE:
 * 				evt->velocity = 0;
 * 				break;
 *
 * 			default: //ignore system/manufacture messages
 * 				evt->type = 0;
 * 				break;
 * 		}
 * 	}
 */


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
        ?.let { it(deltaTime) }
        ?: throw InvalidMidiDataException("Unknown midi voice message type: ${(status and 0xf0).toString(16)}")

    private fun parseNoteOff(deltaTime: Int): MidiVoiceMessage.NoteOff {
        TODO()
    }

    private fun parseNoteOn(deltaTime: Int): MidiVoiceMessage.NoteOn {
        TODO()
    }

    private fun parseKeyPressure(deltaTime: Int): MidiVoiceMessage.KeyPressure {
        TODO()
    }

    private fun parseControlChange(deltaTime: Int): MidiVoiceMessage.ControlChange {
        TODO()
    }

    private fun parseProgramChange(deltaTime: Int): MidiVoiceMessage.ProgramChange {
        TODO()
    }

    private fun parseChannelPressure(deltaTime: Int): MidiVoiceMessage.ChannelPressure {
        TODO()
    }

    private fun parsePitchBend(deltaTime: Int): MidiVoiceMessage.PitchBend {
        TODO()
    }
}

