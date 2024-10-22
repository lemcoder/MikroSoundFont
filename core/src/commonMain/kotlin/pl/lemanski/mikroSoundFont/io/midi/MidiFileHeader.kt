package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.io.readShortAt

data class MidiFileHeader(
    val trackCount: Int,
    val division: Int
) {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun fromBytes(buffer: ByteArray): MidiFileHeader {
            val trackCount: Int
            val division: Int

            if (buffer.size < 14) {
                throw InvalidMidiDataException("Unexpected buffer size. Buffer is too small.")
            }

            val headerBytes = buffer.copyOfRange(0, 14)

            if (headerBytes.copyOfRange(0, 4).decodeToString() != "MThd" || headerBytes[7] != 6.toByte() || headerBytes[9] > 2) {
                throw InvalidMidiDataException("Invalid MThd header: ${headerBytes.toHexString()}")
            }

            if (headerBytes[12].toInt() and 0x80 != 0) {
                throw InvalidMidiDataException("Unsupported SMPTE timing: ${headerBytes.toHexString()}")
            }

            trackCount = headerBytes.readShortAt(10).toInt()
            division = headerBytes.readShortAt(12).toInt() // ticks per beat

            if (trackCount <= 0 ) {
                throw InvalidMidiDataException("Invalid track values: $trackCount")
            }

            if (division <= 0) {
                throw InvalidMidiDataException("Invalid division values: $division")
            }

            return MidiFileHeader(
                trackCount = trackCount,
                division = division
            )
        }
    }
}
