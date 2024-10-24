package pl.lemanski.mikroSoundFont.midi

sealed class MidiMessageType {

    data class NoteOn(
        val key: Int,
        val velocity: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0x90
        }
    }

    data class NoteOff(
        val key: Int,
        val velocity: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0x80
        }

    }

    data class KeyPressure(
        val key: Int,
        val keyPressure: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0xA0
        }

    }

    data class ControlChange(
        val control: Int,
        val controlValue: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0xB0
        }

    }

    data class ProgramChange(
        val program: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0xC0
        }

    }

    data class ChannelPressure(
        val channelPressure: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0xD0
        }
    }

    data class PitchBend(
        val pitchBend: Int
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0xE0
        }
    }

    data class SetTempo(
        val tempo: ByteArray
    ) : MidiMessageType() {
        companion object {
            const val TYPE = 0x51
        }
    }

    data object Unsupported : MidiMessageType() {
        const val TYPE = 0
    }
}

data class MidiMessage(
    val time: Int,
    val type: MidiMessageType,
    val channel: Int,
)

class MidiMessageBuilder {
    private var time: Int = 0
    private var type: MidiMessageType? = null
    private var channel: Int = 0

    fun hasType(): Boolean = type != null

    fun setTime(time: Int) = apply { this.time = time }
    fun setType(type: MidiMessageType) = apply { this.type = type }
    fun setChannel(channel: Int) = apply { this.channel = channel }

    fun build(): MidiMessage = MidiMessage(
        time = time,
        type = type ?: MidiMessageType.Unsupported,
        channel = channel,
    )
}