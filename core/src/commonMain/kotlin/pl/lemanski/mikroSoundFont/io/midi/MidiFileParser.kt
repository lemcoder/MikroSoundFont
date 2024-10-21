package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.getLogger
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessage.Type
import pl.lemanski.mikroSoundFont.midi.MidiMessageControlChange
import pl.lemanski.mikroSoundFont.midi.MidiMessageNoteOff
import pl.lemanski.mikroSoundFont.midi.MidiMessageNoteOn
import pl.lemanski.mikroSoundFont.midi.MidiMessageProgramChange

class MidiFileParser {
    private val logger = getLogger()

    fun parseMidiFile(data: ByteArray): List<MidiMessage> {
        val header = parseMidiHeader(data)
        val events = mutableListOf<MidiMessage>()

        var trackStart = 14 // Header ends at byte 14
        repeat(header.numTracks) {
            events.addAll(parseTrack(data, trackStart))
            trackStart += 8 // Skip over track header and length (MTrk + track length)
        }

        return events
    }

    private fun readVariableLengthQuantity(data: ByteArray, index: Int): Pair<Int, Int> {
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

    private fun parseMidiHeader(data: ByteArray): MidiFileHeader {
        val chunkType = data.sliceArray(0..3).map { it.toInt().toChar() }.joinToString("")
        if (chunkType != "MThd") throw IllegalArgumentException("Invalid MIDI header")

        val format = ((data[8].toInt() and 0xFF) shl 8) or (data[9].toInt() and 0xFF)
        val numTracks = ((data[10].toInt() and 0xFF) shl 8) or (data[11].toInt() and 0xFF)
        val division = ((data[12].toInt() and 0xFF) shl 8) or (data[13].toInt() and 0xFF)

        return MidiFileHeader(format, numTracks, division)
    }

    fun parseTrack(data: ByteArray, trackStart: Int): List<MidiMessage> {
        val events = mutableListOf<MidiMessage>()
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

            when (command.toType()) {
                Type.NOTE_OFF         -> {
                    val key = data[index].toInt() and 0xFF
                    events.add(MidiMessageNoteOff(deltaTime, channel, key))
                    index += 2
                }
                Type.NOTE_ON          -> {
                    val key = data[index].toInt() and 0xFF
                    val velocity = data[index + 1].toInt() and 0xFF
                    events.add(MidiMessageNoteOn(deltaTime, channel, key, velocity))
                    index += 2
                }
                Type.PROGRAM_CHANGE   -> {
                    val program = data[index].toInt() and 0xFF
                    events.add(MidiMessageProgramChange(deltaTime, channel, program))
                    index++
                }
                Type.CONTROL_CHANGE   -> {
                    val control = data[index].toInt() and 0xFF
                    val value = data[index + 1].toInt() and 0xFF
                    events.add(MidiMessageControlChange(deltaTime, channel, control, value))
                    index += 2
                }
                Type.KEY_PRESSURE     -> {
                    logger.log("KEY_PRESSURE")
                }
                Type.CHANNEL_PRESSURE -> {
                    logger.log("CHANNEL_PRESSURE")
                }
                Type.PITCH_BEND       -> {
                    logger.log("PITCH_BEND")
                }
                Type.SET_TEMPO        -> {
                    logger.log("SET_TEMPO")
                }
            }
        }
        return events
    }

    private fun Int.toType(): Type = Type.entries.find { it.value == this } ?: throw IllegalArgumentException("Unknown MIDI event: $this")
}