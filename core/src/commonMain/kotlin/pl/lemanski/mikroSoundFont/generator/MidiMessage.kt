package pl.lemanski.mikroSoundFont.generator

sealed interface MidiMessage {
    val time: Int
    val channel: Int
    val type: Type
    var next: MidiMessage?

    enum class Type(val value: Int) {
        NOTE_OFF(0x80),
        NOTE_ON(0x90),
        KEY_PRESSURE(0xA0),
        CONTROL_CHANGE(0xB0),
        PROGRAM_CHANGE(0xC0),
        CHANNEL_PRESSURE(0xD0),
        PITCH_BEND(0xE0),
        SET_TEMPO(0x51)
    }
}

// ---

data class MidiMessageNoteOn(
    override val time: Int,
    override val channel: Int,
    val key: Int,
    val velocity: Int,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.NOTE_ON
}

// ---

data class MidiMessageNoteOff(
    override val time: Int,
    override val channel: Int,
    val key: Int,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.NOTE_OFF
}

// ---

data class MidiMessageProgramChange(
    override val time: Int,
    override val channel: Int,
    val program: Int,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.PROGRAM_CHANGE
}

// ---

data class MidiMessagePitchBend(
    override val time: Int,
    override val channel: Int,
    val pitchBend: Int,
    override var next: MidiMessage?,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.PITCH_BEND
}

// ---

data class MidiMessageControlChange(
    override val time: Int,
    override val channel: Int,
    val control: Int,
    val controlValue: Int,
    override var next: MidiMessage?,
): MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.CONTROL_CHANGE
}