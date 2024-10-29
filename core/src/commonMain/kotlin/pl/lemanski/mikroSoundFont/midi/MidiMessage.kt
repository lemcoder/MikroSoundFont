package pl.lemanski.mikroSoundFont.midi

/**
 * MIDI meta messages
 * *------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*
 * | Message             | Meta Type | Data Length | Contains                                                       | Occurs At                                                   |
 * |---------------------|-----------|-------------|----------------------------------------------------------------|-------------------------------------------------------------|
 * | Sequence number     | 0x00      | 2 bytes     | The number of a sequence                                       | At delta time 0                                             |
 * | Text                | 0x01      | Variable    | Some text                                                      | Anywhere                                                    |
 * | Copyright notice    | 0x02      | Variable    | A copyright notice                                             | At delta time 0 in the first track                          |
 * | Track name          | 0x03      | Variable    | A track name                                                   | At delta time 0                                             |
 * | Instrument name     | 0x04      | Variable    | The name of an instrument in the current track                 | Anywhere                                                    |
 * | Lyrics              | 0x05      | Variable    | Lyrics, usually a syllable per quarter note                    | Anywhere                                                    |
 * | Marker              | 0x06      | Variable    | The text of a marker                                           | Anywhere                                                    |
 * | Cue point           | 0x07      | Variable    | The text of a cue, usually to prompt for some action from user | Anywhere                                                    |
 * | Channel prefix      | 0x20      | 1 byte      | A channel number (following meta messages apply to this chan)  | Anywhere                                                    |
 * | End of track        | 0x2F      | 0           | -                                                              | At the end of each track                                    |
 * | Set tempo           | 0x51      | 3 bytes     | The number of microseconds per beat                            | Anywhere, but usually in the first track                    |
 * | SMPTE offset        | 0x54      | 5 bytes     | SMPTE time to denote playback offset from the beginning        | Beginning of a track and in the first track of format type 1|
 * | Time signature      | 0x58      | 4 bytes     | Time signature, metronome clicks, size of a beat in 32nd notes | Anywhere                                                    |
 * | Key signature       | 0x59      | 2 bytes     | A key signature                                                | Anywhere                                                    |
 * | Sequencer specific  | 0x7F      | Variable    | Something specific to the MIDI device manufacturer             | Anywhere                                                    |
 * *------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*
 *
 * MIDI voice messages
 * *------------------------------------------------------------------------------------------------------------------------------------------------------------------------*
 * | Message          | Type | Data Length | Contains                        | Description                                                                                  |
 * |------------------|------|-------------|---------------------------------|----------------------------------------------------------------------------------------------|
 * | Note On          | 0x90 | 2 bytes     | Key, Velocity                   | Starts a note on a specific key with a given velocity (volume).                              |
 * | Note Off         | 0x80 | 2 bytes     | Key, Velocity                   | Stops a note on a specific key, with velocity for how quickly the note is released.          |
 * | Key Pressure     | 0xA0 | 2 bytes     | Key, Key Pressure               | Specifies pressure applied to a key after it’s pressed (aftertouch).                         |
 * | Control Change   | 0xB0 | 2 bytes     | Control, Control Value          | Adjusts a control parameter, such as volume, pan, or modulation on the specified channel.    |
 * | Program Change   | 0xC0 | 1 byte      | Program                         | Changes the instrument or program sound on the specified channel.                            |
 * | Channel Pressure | 0xD0 | 1 byte      | Channel Pressure                | Specifies channel-wide aftertouch (pressure applied across all notes on the channel).        |
 * | Pitch Bend       | 0xE0 | 2 bytes     | Pitch Bend                      | Changes pitch smoothly up or down across the entire channel.                                 |
 * *-------------------------------------------------------------------------------------------------------------------------------------------------------------------------*
 */

/**
 * Represents a MIDI message
 */
sealed interface MidiMessage {
    val type: MidiMessageType
    var time: Int
}

sealed class MidiSystemMessage : MidiMessage {
    data object Sysex : MidiSystemMessage() {
        override val type: MidiMessageType = MidiMessageType.SYS_EX
        override var time: Int = 0
    }

    data object Eox : MidiSystemMessage() {
        override val type: MidiMessageType = MidiMessageType.EOX
        override var time: Int = 0
    }

    // other messages
//        TEXT(0x01),
//        COPYRIGHT(0x02),
//        TRACK_NAME(0x03),
//        INST_NAME(0x04),
//        LYRIC(0x05),
//        MARKER(0x06),
//        CUE_POINT(0x07),
//        SMPTE_OFFSET(0x54),
//        TIME_SIGNATURE(0x58),
//        KEY_SIGNATURE(0x59),
//        SEQUENCER_EVENT(0x7f),
//        TIME_CODE(0xf1),
//        SONG_POSITION(0xf2),
//        SONG_SELECT(0xf3),
//        TUNE_REQUEST(0xf6),
//        SYNC(0xf8),
//        TICK(0xf9),
//        START(0xfa),
//        CONTINUE(0xfb),
//        STOP(0xfc),
//        ACTIVE_SENSING(0xfe),
//        SYSTEM_RESET(0xff)
}

sealed class MidiMetaMessage : MidiMessage {
    data class SetTempo(
        override var time: Int,
        val tempo: Int,
    ) : MidiMetaMessage() {
        override val type: MidiMessageType = MidiMessageType.SET_TEMPO
    }

    data class EndOfTrack(
        override var time: Int
    ) : MidiMetaMessage() {
        override val type: MidiMessageType = MidiMessageType.END_OF_TRACK
    }
}

sealed class MidiVoiceMessage : MidiMessage {
    data class NoteOn(
        override var time: Int,
        val channel: Int,
        val key: Int,
        val velocity: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.NOTE_ON
    }

    data class NoteOff(
        override var time: Int,
        val channel: Int,
        val key: Int,
        val velocity: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.NOTE_OFF
    }

    data class KeyPressure(
        override var time: Int,
        val channel: Int,
        val key: Int,
        val keyPressure: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.KEY_PRESSURE
    }

    data class ControlChange(
        override var time: Int,
        val channel: Int,
        val control: Int,
        val controlValue: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.CONTROL_CHANGE
    }

    data class ProgramChange(
        override var time: Int,
        val channel: Int,
        val program: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.PROGRAM_CHANGE
    }

    data class ChannelPressure(
        override var time: Int,
        val channel: Int,
        val channelPressure: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.CHANNEL_PRESSURE
    }

    data class PitchBend(
        override var time: Int,
        val channel: Int,
        val pitchBend: Int,
    ) : MidiVoiceMessage() {
        override val type: MidiMessageType = MidiMessageType.PITCH_BEND
    }
}

data object UnsupportedMidiMessage : MidiMessage {
    override val type: MidiMessageType = MidiMessageType.UNSUPPORTED
    override var time: Int = 0
}