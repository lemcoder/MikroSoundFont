package pl.lemanski.mikroSoundFont.midi

/**
 * General interface for MIDI message types
 */
enum class MidiMessageType(val value: Int) {
    // Meta messages
    END_OF_TRACK(0x2F),
    SET_TEMPO(0x51),

    // Voice messages
    NOTE_OFF(0x80),
    NOTE_ON(0x90),
    KEY_PRESSURE(0xA0),
    CONTROL_CHANGE(0xB0),
    PROGRAM_CHANGE(0xC0),
    CHANNEL_PRESSURE(0xD0),
    PITCH_BEND(0xE0),

    // System common messages
    SYS_EX(0xF0),
    EOX(0xF7),

    UNSUPPORTED(0x00)
}