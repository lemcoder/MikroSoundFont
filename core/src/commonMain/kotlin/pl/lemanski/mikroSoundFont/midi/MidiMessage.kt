package pl.lemanski.mikroSoundFont.midi

sealed interface MidiMessage {
    val time: Int
    val channel: Int
    val type: Type

    sealed interface Type {

        enum class Message(val value: Int) : Type {
            NOTE_OFF(0x80),
            NOTE_ON(0x90),
            KEY_PRESSURE(0xA0),
            CONTROL_CHANGE(0xB0),
            PROGRAM_CHANGE(0xC0),
            CHANNEL_PRESSURE(0xD0),
            PITCH_BEND(0xE0),
            SET_TEMPO(0x51)
        }

        enum class System(val value: Int) : Type {
            TEXT(0x01),
            COPYRIGHT(0x02),
            TRACK_NAME(0x03),
            INST_NAME(0x04),
            LYRIC(0x05),
            MARKER(0x06),
            CUE_POINT(0x07),
            EOT(0x2f),
            SMPTE_OFFSET(0x54),
            TIME_SIGNATURE(0x58),
            KEY_SIGNATURE(0x59),
            SEQUENCER_EVENT(0x7f),
            SYSEX(0xf0),
            TIME_CODE(0xf1),
            SONG_POSITION(0xf2),
            SONG_SELECT(0xf3),
            TUNE_REQUEST(0xf6),
            EOX(0xf7),
            SYNC(0xf8),
            TICK(0xf9),
            START(0xfa),
            CONTINUE(0xfb),
            STOP(0xfc),
            ACTIVE_SENSING(0xfe),
            SYSTEM_RESET(0xff)
        }

        enum class ControlChange(val controllerNumber: Int) : Type {
            TML_BANK_SELECT_MSB(0x00),
            TML_MODULATIONWHEEL_MSB(0x01),
            TML_BREATH_MSB(0x02),
            TML_FOOT_MSB(0x04),
            TML_PORTAMENTO_TIME_MSB(0x05),
            TML_DATA_ENTRY_MSB(0x06),
            TML_VOLUME_MSB(0x07),
            TML_BALANCE_MSB(0x08),
            TML_PAN_MSB(0x0A),
            TML_EXPRESSION_MSB(0x0B),
            TML_EFFECTS1_MSB(0x0C),
            TML_EFFECTS2_MSB(0x0D),
            TML_GPC1_MSB(0x10),
            TML_GPC2_MSB(0x11),
            TML_GPC3_MSB(0x12),
            TML_GPC4_MSB(0x13),

            TML_BANK_SELECT_LSB(0x20),
            TML_MODULATIONWHEEL_LSB(0x21),
            TML_BREATH_LSB(0x22),
            TML_FOOT_LSB(0x24),
            TML_PORTAMENTO_TIME_LSB(0x25),
            TML_DATA_ENTRY_LSB(0x26),
            TML_VOLUME_LSB(0x27),
            TML_BALANCE_LSB(0x28),
            TML_PAN_LSB(0x2A),
            TML_EXPRESSION_LSB(0x2B),
            TML_EFFECTS1_LSB(0x2C),
            TML_EFFECTS2_LSB(0x2D),
            TML_GPC1_LSB(0x30),
            TML_GPC2_LSB(0x31),
            TML_GPC3_LSB(0x32),
            TML_GPC4_LSB(0x33),

            TML_SUSTAIN_SWITCH(0x40),
            TML_PORTAMENTO_SWITCH(0x41),
            TML_SOSTENUTO_SWITCH(0x42),
            TML_SOFT_PEDAL_SWITCH(0x43),
            TML_LEGATO_SWITCH(0x44),
            TML_HOLD2_SWITCH(0x45),

            TML_SOUND_CTRL1(0x46),
            TML_SOUND_CTRL2(0x47),
            TML_SOUND_CTRL3(0x48),
            TML_SOUND_CTRL4(0x49),
            TML_SOUND_CTRL5(0x4A),
            TML_SOUND_CTRL6(0x4B),
            TML_SOUND_CTRL7(0x4C),
            TML_SOUND_CTRL8(0x4D),
            TML_SOUND_CTRL9(0x4E),
            TML_SOUND_CTRL10(0x4F),

            TML_GPC5(0x50),
            TML_GPC6(0x51),
            TML_GPC7(0x52),
            TML_GPC8(0x53),

            TML_PORTAMENTO_CTRL(0x54),

            TML_FX_REVERB(0x5B),
            TML_FX_TREMOLO(0x5C),
            TML_FX_CHORUS(0x5D),
            TML_FX_CELESTE_DETUNE(0x5E),
            TML_FX_PHASER(0x5F),

            TML_DATA_ENTRY_INCR(0x60),
            TML_DATA_ENTRY_DECR(0x61),
            TML_NRPN_LSB(0x62),
            TML_NRPN_MSB(0x63),
            TML_RPN_LSB(0x64),
            TML_RPN_MSB(0x65),

            TML_ALL_SOUND_OFF(0x78),
            TML_ALL_CTRL_OFF(0x79),
            TML_LOCAL_CONTROL(0x7A),
            TML_ALL_NOTES_OFF(0x7B),
            TML_OMNI_OFF(0x7C),
            TML_OMNI_ON(0x7D),
            TML_POLY_OFF(0x7E),
            TML_POLY_ON(0x7F)
        }
    }
}

// ---

data class MidiMessageNoteOn(
    override val time: Int,
    override val channel: Int,
    val key: Int,
    val velocity: Int,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.Message.NOTE_ON
}

// ---

data class MidiMessageNoteOff(
    override val time: Int,
    override val channel: Int,
    val key: Int,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.Message.NOTE_OFF
}

// ---

data class MidiMessageProgramChange(
    override val time: Int,
    override val channel: Int,
    val program: Int,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.Message.PROGRAM_CHANGE
}

// ---

data class MidiMessagePitchBend(
    override val time: Int,
    override val channel: Int,
    val pitchBend: Int,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.Message.PITCH_BEND
}

// ---

data class MidiMessageControlChange(
    override val time: Int,
    override val channel: Int,
    val control: Int,
    val controlValue: Int,
) : MidiMessage {
    override val type: MidiMessage.Type = MidiMessage.Type.Message.CONTROL_CHANGE
}