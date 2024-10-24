package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.io.readByteAt
import pl.lemanski.mikroSoundFont.io.readVariableLengthAt

class MidiMessageParser(
    private val buffer: ByteArray
) {
    private var lastStatus: Int = 0

    fun parse(): List<MidiMessage> {
        val messages = mutableListOf<MidiMessage>()
        var pos = 0

        while (pos < buffer.size) {
            val (message, newPos) = parseMessageAt(pos, buffer) ?: break
            messages.add(message)
            pos = newPos
        }

        return messages
    }

    private fun parseMessageAt(pos: Int, buffer: ByteArray): Pair<MidiMessage, Int>? {
        val message = MidiMessageBuilder()

        var p = pos
        var (dt, newPos) = buffer.readVariableLengthAt(p) ?: return null
        p = newPos
        var status = buffer.readByteAt(p).toInt() and 0xFF
        p++

        //throw away delays that are insanely high for malformatted midis
        if (dt.toUInt() > 4_293_918_720u) {
            dt = 0
        }

        if (status < 0) {
            return null
        }

        if ((status and 0x80) == 0) {
            if ((lastStatus and 0x80) == 0) {
                return null
            }
            p--
            status = lastStatus
        } else {
            lastStatus = status
        }

        if (status == SystemType.SYSEX.type || status == SystemType.EOX.type) {
            p = buffer.readVariableLengthAt(p)?.second ?: return null
            message.setType(MidiMessageType.Unsupported)
        } else if (status == metaEventStatus) {
            val metaType = buffer.readByteAt(p).toInt() and 0xFF
            p++
            if (metaType < 0) {
                return null
            }

            val (buflen, np) = buffer.readVariableLengthAt(p) ?: return null
            p = np

            if (buflen > 0 && p + buflen > buffer.size) {
                return null
            }

            when (metaType) {
                SystemType.EOT.type           -> {
                    if (buflen != 0) {
                        return null
                    }
                }
                MidiMessageType.SetTempo.TYPE -> {
                    if (buflen != 3) {
                        return null
                    }
                    val tempo = ByteArray(3)
                    tempo[0] = buffer.readByteAt(p)
                    tempo[1] = buffer.readByteAt(p + 1)
                    tempo[2] = buffer.readByteAt(p + 2)

                    message.setType(MidiMessageType.SetTempo(tempo))
                }
                else                          -> {
                    message.setType(MidiMessageType.Unsupported)
                }
            }
        } else {
            val data1 = buffer.readByteAt(p).toInt() and 0xFF
            p++
            if (data1 < 0) {
                return null
            }
            message.setChannel(status and 0x0f)
            val eventType = status and 0xf0
            val param = data1 and 0x7f

            when (eventType) {
                MidiMessageType.NoteOff.TYPE         -> {
                    val data2 = buffer.readByteAt(p).toInt() and 0xFF
                    p++
                    if (data2 < 0) {
                        return null
                    }
                    val velocity = data2 and 0x7f
                    message.setType(MidiMessageType.NoteOff(key = param, velocity = velocity))
                }
                MidiMessageType.NoteOn.TYPE          -> {
                    val data2 = buffer.readByteAt(p).toInt() and 0xFF
                    p++
                    if (data2 < 0) {
                        return null
                    }
                    val velocity = data2 and 0x7f
                    message.setType(MidiMessageType.NoteOn(key = param, velocity = velocity))

                }
                MidiMessageType.KeyPressure.TYPE     -> {
                    val data2 = buffer.readByteAt(p).toInt() and 0xFF
                    p++
                    if (data2 < 0) {
                        return null
                    }
                    val keyPressure = data2 and 0x7f
                    message.setType(MidiMessageType.KeyPressure(key = param, keyPressure = keyPressure))

                }
                MidiMessageType.ControlChange.TYPE   -> {
                    val data2 = buffer.readByteAt(p).toInt() and 0xFF
                    p++
                    if (data2 < 0) {
                        return null
                    }
                    val value = data2 and 0x7f
                    message.setType(MidiMessageType.ControlChange(control = param, controlValue = value))
                }
                MidiMessageType.PitchBend.TYPE       -> {
                    val data2 = buffer.readByteAt(p).toInt() and 0xFF
                    p++
                    if (data2 < 0) {
                        return null
                    }
                    val value = data2 and 0x7f
                    message.setType(MidiMessageType.PitchBend(pitchBend = (value shl 7) or param))
                }
                MidiMessageType.ProgramChange.TYPE   -> {
                    message.setType(MidiMessageType.ProgramChange(program = param))
                }
                MidiMessageType.ChannelPressure.TYPE -> {
                    message.setType(MidiMessageType.ChannelPressure(channelPressure = param))
                }
                else                                 -> {
                    message.setType(MidiMessageType.Unsupported)
                }
            }

        }

        if (dt > 0 || message.hasType()) {
            message.setTime(dt)
        }

        return message.build() to p
    }

    //---

    private enum class SystemType(val type: Int) {
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
    };

    private val metaEventStatus = 0xff
}