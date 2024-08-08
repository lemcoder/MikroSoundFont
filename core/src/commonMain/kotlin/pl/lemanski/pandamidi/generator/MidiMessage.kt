package pl.lemanski.pandamidi.generator

sealed interface MidiMessage {
    val time: UInt
    val channel: UInt
    val type: Type
    var next: MidiMessage?

    enum class Type(val value: UInt) {
        NOTE_OFF(0x80u),
        NOTE_ON(0x90u),
        KEY_PRESSURE(0xA0u),
        CONTROL_CHANGE(0xB0u),
        PROGRAM_CHANGE(0xC0u),
        CHANNEL_PRESSURE(0xD0u),
        PITCH_BEND(0xE0u),
        SET_TEMPO(0x51u)
    }
}

// ---

data class MidiMessageNoteOn(
    override val time: UInt,
    override val channel: UInt,
    val key: UInt,
    val velocity: UInt,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.NOTE_ON
}

// ---

data class MidiMessageNoteOff(
    override val time: UInt,
    override val channel: UInt,
    val key: UInt,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.NOTE_OFF
}

// ---

data class MidiMessageProgramChange(
    override val time: UInt,
    override val channel: UInt,
    val program: UInt,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.PROGRAM_CHANGE
}

// ---

data class MidiMessagePitchBend(
    override val time: UInt,
    override val channel: UInt,
    val pitchBend: UInt,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.PITCH_BEND
}

// ---

data class MidiMessageControlChange(
    override val time: UInt,
    override val channel: UInt,
    val control: UInt,
    val controlValue: UInt,
    override var next: MidiMessage?,
): MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.CONTROL_CHANGE
}