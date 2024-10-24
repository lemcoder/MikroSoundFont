package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.io.readIntAt

data class MidiTrack(
    val rawData: ByteArray,
    val messages: List<MidiMessage>
) {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun fromBytes(buffer: ByteArray): MidiTrack {
            if (buffer.size < 8) {
                throw InvalidMidiDataException("Unexpected EOF. Track header is too short")
            }

            val trackHeader = buffer.copyOfRange(0, 8)
            if (trackHeader.decodeToString(0, 4) != "MTrk") {
                throw InvalidMidiDataException("Invalid MTrk header: ${trackHeader.toHexString()}")
            }

            val trackLength = buffer.readIntAt(4)
            if (trackLength < 0) {
                throw InvalidMidiDataException("Invalid MTrk header.Track length is negative: ${trackHeader.toHexString()}")
            }

            if (buffer.size < 8 + trackLength) {
                throw InvalidMidiDataException("Unexpected EOF. Track data is too short")
            }

            val rawData = buffer.copyOfRange(8, 8 + trackLength)
            val messages = MidiMessageParser(rawData).parse()

            return MidiTrack(rawData, messages)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MidiTrack

        if (!rawData.contentEquals(other.rawData)) return false
        if (messages != other.messages) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rawData.contentHashCode()
        result = 31 * result + messages.hashCode()
        return result
    }
}