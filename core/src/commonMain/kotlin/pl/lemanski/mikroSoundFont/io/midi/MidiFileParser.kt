package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.midi.EventType
import pl.lemanski.mikroSoundFont.midi.MidiEvent

class MidiFileParser {
    fun parseMidiFile(data: ByteArray): List<MidiEvent> {
        val header = parseMidiHeader(data)
        val events = mutableListOf<MidiEvent>()

        var trackStart = 14 // Header ends at byte 14
        repeat(header.numTracks) {
            events.addAll(parseTrack(data, trackStart))
            trackStart += 8 // Skip over track header and length (MTrk + track length)
        }

        return events
    }

    fun readVariableLengthQuantity(data: ByteArray, index: Int): Pair<Int, Int> {
        var value = 0
        var i = index
        var byte: Int

        do {
            byte = data[i].toInt() and 0xFF
            value = (value shl 7) or (byte and 0x7F)
            i++
        } while (byte and 0x80 != 0)

        return Pair(value, i)
    }

    fun parseMidiHeader(data: ByteArray): MidiFileHeader {
        val chunkType = data.sliceArray(0..3).map { it.toInt().toChar() }.joinToString("")
        if (chunkType != "MThd") throw IllegalArgumentException("Invalid MIDI header")

        val format = ((data[8].toInt() and 0xFF) shl 8) or (data[9].toInt() and 0xFF)
        val numTracks = ((data[10].toInt() and 0xFF) shl 8) or (data[11].toInt() and 0xFF)
        val division = ((data[12].toInt() and 0xFF) shl 8) or (data[13].toInt() and 0xFF)

        return MidiFileHeader(format, numTracks, division)
    }

    fun parseTrack(data: ByteArray, trackStart: Int): List<MidiEvent> {
        val events = mutableListOf<MidiEvent>()
        var index = trackStart + 8 // Skip "MTrk" and track length
        var lastStatusByte = 0

        while (index < data.size) {
            // Read delta time
            val (deltaTime, newIndex) = readVariableLengthQuantity(data, index)
            index = newIndex

            val statusByte = data[index].toInt() and 0xFF
            index++

            // MIDI running status (if the status byte is missing, reuse the last one)
            if (statusByte and 0x80 == 0) {
                index--
            } else {
                lastStatusByte = statusByte
            }

            val command = lastStatusByte and 0xF0
            val channel = lastStatusByte and 0x0F

            when (command) {
                0x90 -> { // Note On
                    val key = data[index].toInt() and 0xFF
                    val velocity = data[index + 1].toInt() and 0xFF
                    events.add(MidiEvent(deltaTime.toLong(), EventType.NOTE_ON, listOf(channel, key, velocity)))
                    index += 2
                }
                0x80 -> { // Note Off
                    val key = data[index].toInt() and 0xFF
                    events.add(MidiEvent(deltaTime.toLong(), EventType.NOTE_OFF, listOf(channel, key)))
                    index += 2
                }
                0xC0 -> { // Program Change
                    val program = data[index].toInt() and 0xFF
                    events.add(MidiEvent(deltaTime.toLong(), EventType.PROGRAM_CHANGE, listOf(channel, program)))
                    index++
                }
                // Add more cases for other types of MIDI events
            }
        }
        return events
    }
}