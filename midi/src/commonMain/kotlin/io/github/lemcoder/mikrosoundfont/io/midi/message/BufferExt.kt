package io.github.lemcoder.mikrosoundfont.io.midi.message

import kotlinx.io.Buffer
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException
import kotlin.experimental.or

internal fun Buffer.readVarLen(): Int {
    var r = 0
    var c: Int
    var bytesRead = 0

    do {
        c = readByte().toInt() and 0xFF
        bytesRead++
        r = (r shl 7) or (c and 0x7F) // add 7 LSB of c to r
    } while (c and 0x80 != 0 && bytesRead <= 4) // until c MSB is 1

    if (bytesRead > 4) {
        throw InvalidMidiDataException("Malformed MIDI. Variable length quantity exceeds 32 bits.")
    }

    return r
}

internal fun Buffer.writeVarLen(value: Int) {
    var buffer = value
    val byteStack = mutableListOf<Byte>()

    do {
        var byte = (buffer and 0x7F).toByte()
        buffer = buffer shr 7

        if (byteStack.isNotEmpty()) {
            byte = (byte or 0x80.toByte())
        }

        byteStack.add(0, byte)
    } while (buffer > 0)

    for (b in byteStack) {
        writeByte(b)
    }
}