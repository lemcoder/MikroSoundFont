package pl.lemanski.mikroSoundFont.io.midi.message

import kotlinx.io.Buffer
import pl.lemanski.mikroSoundFont.InvalidMidiDataException

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