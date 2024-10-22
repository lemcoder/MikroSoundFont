package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.getLogger
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessage.Type

class MidiFileParser {
    private val logger = getLogger()

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

    fun parseTrack(data: ByteArray, trackStart: Int): List<MidiMessage> {
        val events = mutableListOf<MidiMessage>()
        var index = trackStart + 8 // Skip "MTrk" and track length
        var lastStatusByte = 0

        while (index < data.size) {
            // Read delta time
            val (deltaTime, newIndex) = readVariableLengthQuantity(data, index)
            index = newIndex

            if (index >= data.size - 1) break

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
                Type.ControlChange.TML_BANK_SELECT_MSB     -> TODO()
                Type.ControlChange.TML_MODULATIONWHEEL_MSB -> TODO()
                Type.ControlChange.TML_BREATH_MSB          -> TODO()
                Type.ControlChange.TML_FOOT_MSB            -> TODO()
                Type.ControlChange.TML_PORTAMENTO_TIME_MSB -> TODO()
                Type.ControlChange.TML_DATA_ENTRY_MSB      -> TODO()
                Type.ControlChange.TML_VOLUME_MSB          -> TODO()
                Type.ControlChange.TML_BALANCE_MSB         -> TODO()
                Type.ControlChange.TML_PAN_MSB             -> TODO()
                Type.ControlChange.TML_EXPRESSION_MSB      -> TODO()
                Type.ControlChange.TML_EFFECTS1_MSB        -> TODO()
                Type.ControlChange.TML_EFFECTS2_MSB        -> TODO()
                Type.ControlChange.TML_GPC1_MSB            -> TODO()
                Type.ControlChange.TML_GPC2_MSB            -> TODO()
                Type.ControlChange.TML_GPC3_MSB            -> TODO()
                Type.ControlChange.TML_GPC4_MSB            -> TODO()
                Type.ControlChange.TML_BANK_SELECT_LSB     -> TODO()
                Type.ControlChange.TML_MODULATIONWHEEL_LSB -> TODO()
                Type.ControlChange.TML_BREATH_LSB          -> TODO()
                Type.ControlChange.TML_FOOT_LSB            -> TODO()
                Type.ControlChange.TML_PORTAMENTO_TIME_LSB -> TODO()
                Type.ControlChange.TML_DATA_ENTRY_LSB      -> TODO()
                Type.ControlChange.TML_VOLUME_LSB          -> TODO()
                Type.ControlChange.TML_BALANCE_LSB         -> TODO()
                Type.ControlChange.TML_PAN_LSB             -> TODO()
                Type.ControlChange.TML_EXPRESSION_LSB      -> TODO()
                Type.ControlChange.TML_EFFECTS1_LSB        -> TODO()
                Type.ControlChange.TML_EFFECTS2_LSB        -> TODO()
                Type.ControlChange.TML_GPC1_LSB            -> TODO()
                Type.ControlChange.TML_GPC2_LSB            -> TODO()
                Type.ControlChange.TML_GPC3_LSB            -> TODO()
                Type.ControlChange.TML_GPC4_LSB            -> TODO()
                Type.ControlChange.TML_SUSTAIN_SWITCH      -> TODO()
                Type.ControlChange.TML_PORTAMENTO_SWITCH   -> TODO()
                Type.ControlChange.TML_SOSTENUTO_SWITCH    -> TODO()
                Type.ControlChange.TML_SOFT_PEDAL_SWITCH   -> TODO()
                Type.ControlChange.TML_LEGATO_SWITCH       -> TODO()
                Type.ControlChange.TML_HOLD2_SWITCH        -> TODO()
                Type.ControlChange.TML_SOUND_CTRL1         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL2         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL3         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL4         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL5         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL6         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL7         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL8         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL9         -> TODO()
                Type.ControlChange.TML_SOUND_CTRL10        -> TODO()
                Type.ControlChange.TML_GPC5                -> TODO()
                Type.ControlChange.TML_GPC6                -> TODO()
                Type.ControlChange.TML_GPC7                -> TODO()
                Type.ControlChange.TML_GPC8                -> TODO()
                Type.ControlChange.TML_PORTAMENTO_CTRL     -> TODO()
                Type.ControlChange.TML_FX_REVERB           -> TODO()
                Type.ControlChange.TML_FX_TREMOLO          -> TODO()
                Type.ControlChange.TML_FX_CHORUS           -> TODO()
                Type.ControlChange.TML_FX_CELESTE_DETUNE   -> TODO()
                Type.ControlChange.TML_FX_PHASER           -> TODO()
                Type.ControlChange.TML_DATA_ENTRY_INCR     -> TODO()
                Type.ControlChange.TML_DATA_ENTRY_DECR     -> TODO()
                Type.ControlChange.TML_NRPN_LSB            -> TODO()
                Type.ControlChange.TML_NRPN_MSB            -> TODO()
                Type.ControlChange.TML_RPN_LSB             -> TODO()
                Type.ControlChange.TML_RPN_MSB             -> TODO()
                Type.ControlChange.TML_ALL_SOUND_OFF       -> TODO()
                Type.ControlChange.TML_ALL_CTRL_OFF        -> TODO()
                Type.ControlChange.TML_LOCAL_CONTROL       -> TODO()
                Type.ControlChange.TML_ALL_NOTES_OFF       -> TODO()
                Type.ControlChange.TML_OMNI_OFF            -> TODO()
                Type.ControlChange.TML_OMNI_ON             -> TODO()
                Type.ControlChange.TML_POLY_OFF            -> TODO()
                Type.ControlChange.TML_POLY_ON             -> TODO()
                Type.Message.NOTE_OFF                      -> TODO()
                Type.Message.NOTE_ON                       -> TODO()
                Type.Message.KEY_PRESSURE                  -> TODO()
                Type.Message.CONTROL_CHANGE                -> TODO()
                Type.Message.PROGRAM_CHANGE                -> TODO()
                Type.Message.CHANNEL_PRESSURE              -> TODO()
                Type.Message.PITCH_BEND                    -> TODO()
                Type.Message.SET_TEMPO                     -> TODO()
                Type.System.TEXT                           -> TODO()
                Type.System.COPYRIGHT                      -> TODO()
                Type.System.TRACK_NAME                     -> TODO()
                Type.System.INST_NAME                      -> TODO()
                Type.System.LYRIC                          -> TODO()
                Type.System.MARKER                         -> TODO()
                Type.System.CUE_POINT                      -> TODO()
                Type.System.EOT                            -> TODO()
                Type.System.SMPTE_OFFSET                   -> TODO()
                Type.System.TIME_SIGNATURE                 -> TODO()
                Type.System.KEY_SIGNATURE                  -> TODO()
                Type.System.SEQUENCER_EVENT                -> TODO()
                Type.System.SYSEX                          -> TODO()
                Type.System.TIME_CODE                      -> TODO()
                Type.System.SONG_POSITION                  -> TODO()
                Type.System.SONG_SELECT                    -> TODO()
                Type.System.TUNE_REQUEST                   -> TODO()
                Type.System.EOX                            -> TODO()
                Type.System.SYNC                           -> TODO()
                Type.System.TICK                           -> TODO()
                Type.System.START                          -> TODO()
                Type.System.CONTINUE                       -> TODO()
                Type.System.STOP                           -> TODO()
                Type.System.ACTIVE_SENSING                 -> TODO()
                Type.System.SYSTEM_RESET                   -> TODO()
            }
        }
        return events
    }

    private fun Int.toType(): Type {
        println("event: $this")
        Type.ControlChange.entries.find { it.controllerNumber == this }?.let { return it }
        Type.Message.entries.find { it.value == this }?.let { return it }
        Type.System.entries.find { it.value == this }?.let { return it }

        throw InvalidMidiDataException("Unknown event type: $this")
    }

    private fun readVariableLength(buffer: ByteArray, startIndex: Int): Pair<Int, Int> {
        var result = 0
        var index = startIndex // Track current position in the array
        var i = 0

        while (i < 4) {
            if (index >= buffer.size) {
                throw IllegalArgumentException("Unexpected end of file")
            }

            val c = buffer[index]
            index++ // Move the pointer to the next byte

            if (c.toInt() and 0x80 != 0) {
                result = (result or (c.toInt() and 0x7F)) shl 7
            } else {
                return Pair(result or c.toInt(), index) // Return the result and the new index
            }

            i++
        }

        throw IllegalArgumentException("Invalid variable length byte count")
    }
}