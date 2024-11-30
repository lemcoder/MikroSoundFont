package io.github.lemcoder.mikrosoundfont.io.midi

import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.io.readUInt
import kotlinx.io.readUShort
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException
import io.github.lemcoder.mikrosoundfont.io.midi.message.MidiMessageContext
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiTrack

internal class MidiFileParser(
    private val data: ByteArray,
) {
    private val buffer = Buffer().apply {
        write(data)
    }

    fun parse(): MidiFile {
        val header = parseHeader()
        val tracks = parseTracks(header.trackCount)

        return MidiFile(
            header = header,
            tracks = tracks
        )
    }

    internal fun parseHeader(): MidiFile.Header {
        val hSize = 14

        if (buffer.size < hSize) {
            throw InvalidMidiDataException("Unexpected buffer size. Buffer is too small.")
        }

        if (buffer.readString(4) != "MThd") {
            throw InvalidMidiDataException("Invalid MThd header: $buffer")
        }

        if (buffer.readUInt() != 6u) {
            throw InvalidMidiDataException("Invalid MThd header: $buffer")
        }

        if (buffer.readUShort() > 2u) {
            throw InvalidMidiDataException("Invalid MThd header: $buffer")
        }

        val trackCount: Int = buffer.readShort().toInt()
        val division: Int = buffer.readShort().toInt() // ticks per beat

        if ((division shl 8) and 0x80 != 0) {
            throw InvalidMidiDataException("Unsupported SMPTE timing: $buffer")
        }

        if (trackCount <= 0) {
            throw InvalidMidiDataException("Invalid track values: $trackCount")
        }

        if (division <= 0) {
            throw InvalidMidiDataException("Invalid division values: $division")
        }

        return MidiFile.Header(
            trackCount = trackCount,
            division = division
        )
    }

    internal fun parseTracks(n: Int): List<MidiTrack> = List(n) { parseTrack() }

    internal fun parseTrack(): MidiTrack {
        val trackHeader = parseTrackHeader()
        val trackBuffer = Buffer()
        val messages = mutableListOf<MidiMessage>()

        buffer.readAtMostTo(trackBuffer, trackHeader.dataSize.toLong())

        val messageContext = MidiMessageContext(trackBuffer)
        while (trackBuffer.size > 0) {
            messages.add(messageContext.readMessage())
        }

        return MidiTrack(
            header = trackHeader,
            messages = messages
        )
    }

    internal fun parseTrackHeader(): MidiTrack.Header {
        if (buffer.readString(4) != "MTrk") {
            throw InvalidMidiDataException("Invalid MTrk header")
        }

        val trackLength = buffer.readInt()
        if (trackLength < 0) {
            throw InvalidMidiDataException("Invalid MTrk header.Track length is negative.")
        }

        return MidiTrack.Header(dataSize = trackLength)
    }
}